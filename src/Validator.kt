fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(email)
}
fun isValidPhone(phone: String): Boolean {
    val phoneRegex = "^(\\+7|7|8)?[0-9]{10}$".toRegex()
    val cleanPhone = phone.replace(Regex("[\\s()-]"), "")
    return phoneRegex.matches(cleanPhone)
}
