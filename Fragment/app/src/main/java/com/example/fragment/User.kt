package com.example.fragment

class User private constructor(private val name: String) {

    private var age = 0

    private fun printHello() {
        LogUtil.d(TAG, "Hello $name $age")
    }

    companion object {
        private const val TAG = "User"

        fun createUser(email: String) = User(email.substringBefore('@'))

        fun testMethod(user: User) {
            user.age = 27
            user.printHello()
        }

    }
}