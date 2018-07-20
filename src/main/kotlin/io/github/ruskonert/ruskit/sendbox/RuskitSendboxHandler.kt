package io.github.ruskonert.ruskit.sendbox

class RuskitSendboxHandler : ExternalExecutor()
{
    override fun onInit(handleInstance: Any?): Any?
    {
        return super.onInit(this)
    }

    companion object
    {
        private val instance : RuskitSendboxHandler = RuskitSendboxHandler()
        @JvmStatic
        fun getInstance() : RuskitSendboxHandler = instance

        @JvmStatic
        @SafetyExecutable("Ruskit.Test")
        private external fun Test0(a: String)

        @JvmStatic
        @SafetyExecutable("Ruskit.Test")
        private external fun ConsoleClear0()

        @JvmStatic
        @SafetyExecutable("Ruskit.Test")
        private external fun PlaySoundA(c: ByteArray)
    }
}