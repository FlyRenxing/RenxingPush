package top.imzdx.renxingpush.utils

import cn.hutool.core.lang.Console.input
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.Bot
import net.mamoe.mirai.auth.QRCodeLoginListener
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.utils.*
import java.io.File
import javax.imageio.ImageIO

class ReLoginSolver : LoginSolver() {
    private val loggerSupplier: (bot: Bot) -> MiraiLogger = { it.logger }
    private val loginSolverLock = Mutex()
    lateinit var verificationResult: CompletableDeferred<String>
    var url: String = ""
    var qrcodeFile: File? = null
    var qrcodeNeedUpdate: Boolean = false
    override fun createQRCodeLoginListener(bot: Bot): QRCodeLoginListener {
        return object : QRCodeLoginListener {
            private var tmpFile: File? = null

            override val qrCodeMargin: Int get() = 1
            override val qrCodeSize: Int get() = 1

            override fun onFetchQRCode(bot: Bot, data: ByteArray) {
                val logger = loggerSupplier(bot)

                logger.info { "[QRCodeLogin] 已获取登录二维码，请在手机 QQ 使用账号 ${bot.id} 扫码" }
                logger.info { "[QRCodeLogin] Fetched login qrcode, please scan via qq android with account ${bot.id}." }

                try {
                    val tempFile: File
                    if (tmpFile == null) {
                        tempFile = File.createTempFile(
                            "mirai-qrcode-${bot.id}-${currentTimeSeconds()}",
                            ".png"
                        ).apply { deleteOnExit() }

                        tempFile.createNewFile()

                        tmpFile = tempFile
                    } else {
                        tempFile = tmpFile!!
                    }
                    tempFile.writeBytes(data)
                    qrcodeFile = tempFile
                    logger.info {
                        "[QRCodeLogin] 将会显示二维码图片，若看不清图片，请查看文件 ${
                            tempFile.toPath().toUri()
                        }"
                    }
                    logger.info {
                        "[QRCodeLogin] Displaying qrcode image. If not clear, view file ${
                            tempFile.toPath().toUri()
                        }."
                    }
                } catch (e: Exception) {
                    logger.warning("[QRCodeLogin] 无法写出二维码图片. 请尽量关闭终端个性化样式后扫描二维码字符图片", e)
                    logger.warning(
                        "[QRCodeLogin] Failed to export qrcode image. Please try to scan the char-image after disabling custom terminal style.",
                        e
                    )
                }

                data.inputStream().use { stream ->
                    try {
                        val isCacheEnabled = ImageIO.getUseCache()

                        try {
                            ImageIO.setUseCache(false)
                            val img = ImageIO.read(stream)
                            if (img == null) {
                                logger.warning { "[QRCodeLogin] 无法创建字符图片. 请查看文件" }
                                logger.warning { "[QRCodeLogin] Failed to create char-image. Please see the file." }
                            } else {
//                                logger.info { "[QRCodeLogin] \n" + img.renderQRCode() }
                                logger.warning { "[QRCodeLogin] 无法创建字符图片. 请查看文件" }
                            }
                        } finally {
                            ImageIO.setUseCache(isCacheEnabled)
                        }

                    } catch (throwable: Throwable) {
                        logger.warning("[QRCodeLogin] 创建字符图片时出错. 请查看文件.", throwable)
                        logger.warning("[QRCodeLogin] Failed to create char-image. Please see the file.", throwable)
                    }
                }
            }

            override fun onStateChanged(bot: Bot, state: QRCodeLoginListener.State) {
                val logger = loggerSupplier(bot)
                logger.info {
                    buildString {
                        append("[QRCodeLogin] ")
                        when (state) {
                            QRCodeLoginListener.State.WAITING_FOR_SCAN -> append("等待扫描二维码中")
                            QRCodeLoginListener.State.WAITING_FOR_CONFIRM -> append("扫描完成，请在手机 QQ 确认登录")
                            QRCodeLoginListener.State.CANCELLED -> append("已取消登录，将会重新获取二维码")
                            QRCodeLoginListener.State.TIMEOUT -> append("扫描超时，将会重新获取二维码")
                            QRCodeLoginListener.State.CONFIRMED -> append("已确认登录")
                            else -> append("default state")
                        }
                    }
                }
                logger.info {
                    buildString {
                        append("[QRCodeLogin] ")
                        when (state) {
                            QRCodeLoginListener.State.WAITING_FOR_SCAN -> append("Waiting for scanning qrcode.")
                            QRCodeLoginListener.State.WAITING_FOR_CONFIRM -> append("Scan complete. Please confirm login.")
                            QRCodeLoginListener.State.CANCELLED -> append("Login cancelled, we will try to fetch qrcode again.")
                            QRCodeLoginListener.State.TIMEOUT -> append("Timeout scanning, we will try to fetch qrcode again.")
                            QRCodeLoginListener.State.CONFIRMED -> append("Login confirmed.")
                            else -> append("default state")
                        }
                    }
                }
                if (state == QRCodeLoginListener.State.TIMEOUT) {
                    qrcodeNeedUpdate = true
                }
                if (state == QRCodeLoginListener.State.CONFIRMED) {
                    kotlin.runCatching { tmpFile?.delete() }.onFailure { logger.warning(it) }
                }
            }

        }
    }

    private fun currentTimeSeconds(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }


    /**
     * 处理图片验证码, 返回图片验证码内容.
     *
     * 返回 `null` 以表示无法处理验证码, 将会刷新验证码或重试登录.
     *
     * ## 异常类型
     *
     * 抛出一个 [LoginFailedException] 以正常地终止登录, 并可建议系统进行重连或停止 bot (通过 [LoginFailedException.killBot]).
     * 例如抛出 [RetryLaterException] 可让 bot 重新进行一次登录.
     *
     * 抛出任意其他 [Throwable] 将视为验证码解决器的自身错误.
     *
     * @throws LoginFailedException
     */
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        TODO("Not yet implemented")
    }

    /**
     * 处理滑动验证码.
     *
     * 返回 `null` 以表示无法处理验证码, 将会刷新验证码或重试登录.
     *
     * ## 异常类型
     *
     * 抛出一个 [LoginFailedException] 以正常地终止登录, 并可建议系统进行重连或停止 bot (通过 [LoginFailedException.killBot]).
     * 例如抛出 [RetryLaterException] 可让 bot 重新进行一次登录.
     *
     * 抛出任意其他 [Throwable] 将视为验证码解决器的自身错误.
     *
     * @throws LoginFailedException
     * @return 验证码解决成功后获得的 ticket.
     */
    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        //输出url
        println(url)
        //保存url
        this.url = url
        //创建一个CompletableDeferred
        verificationResult = CompletableDeferred()
        //等待用户输入
        return verificationResult.await()

    }

    //TODO 该方法未实现，暂时使用命令行输入
    override suspend fun onSolveDeviceVerification(
        bot: Bot, requests: DeviceVerificationRequests
    ): DeviceVerificationResult {
        val logger = loggerSupplier(bot)
        requests.sms?.let { req ->
            solveSms(logger, req)?.let { return it }
        }
        requests.fallback?.let { fallback ->
            solveFallback(logger, fallback.url)
            return fallback.solved()
        }
        error("User rejected SMS login while fallback login method not available.")
    }

    private suspend fun solveFallback(
        logger: MiraiLogger, url: String
    ): String {
        return loginSolverLock.withLock {
            logger.info { "[UnsafeLogin] 当前登录环境不安全，服务器要求账户认证。请在 QQ 浏览器打开 $url 并完成验证后输入任意字符。" }
            logger.info { "[UnsafeLogin] Account verification required by the server. Please open $url in QQ browser and complete challenge, then type anything here to submit." }
            input().also {
                logger.info { "[UnsafeLogin] 正在提交中..." }
                logger.info { "[UnsafeLogin] Submitting..." }
            }
        }
    }

    private suspend fun solveSms(
        logger: MiraiLogger, request: DeviceVerificationRequests.SmsRequest
    ): DeviceVerificationResult? = loginSolverLock.withLock {
        val countryCode = request.countryCode
        val phoneNumber = request.phoneNumber
        if (countryCode != null && phoneNumber != null) {
            logger.info("一条短信验证码将发送到你的手机 (+$countryCode) $phoneNumber. 运营商可能会收取正常短信费用, 是否继续? 输入 yes 继续, 输入其他终止并尝试其他验证方式.")
            logger.info(
                "A verification code will be send to your phone (+$countryCode) $phoneNumber, which may be charged normally, do you wish to continue? Type yes to continue, type others to cancel and try other methods."
            )
        } else {
            logger.info("一条短信验证码将发送到你的手机 (无法获取到手机号码). 运营商可能会收取正常短信费用, 是否继续? 输入 yes 继续, 输入其他终止并尝试其他验证方式.")
            logger.info(
                "A verification code will be send to your phone (failed to get phone number), " + "which may be charged normally by your carrier, " + "do you wish to continue? Type yes to continue, type others to cancel and try other methods."
            )
        }
        val answer = input().trim()
        return if (answer.equals("yes", ignoreCase = true)) {
            logger.info("Attempting SMS verification.")
            request.requestSms()
            logger.info("Please enter code: ")
            val code = input().trim()
            logger.info("Continuing with code '$code'.")
            request.solved(code)
        } else {
            logger.info("Cancelled.")
            null
        }
    }

}
