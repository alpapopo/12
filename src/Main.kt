fun main() {
    val menedzher = ContactManager()

    while (true) {
        vyvestiMenu()

        when (readLine()) {
            "1" -> menedzher.pokazatVseKontakty()
            "2" -> menedzher.dobavitKontakt()
            "3" -> menedzher.poiskKontakta()
            "4" -> menedzher.upravlenieGruppami()
            "5" -> menedzher.eksportGruppy()
            "0" -> {
                println("\nДо свидания!")
                return
            }
            else -> println("Неверный выбор. Попробуйте снова.")
        }
    }
}

fun vyvestiMenu() {
    println("\n" + "=".repeat(40))
    println("=== КОНТАКТЫ ===")
    println("=".repeat(40))
    println("1. Все контакты")
    println("2. Добавить контакт")
    println("3. Поиск контакта")
    println("4. Управление группами")
    println("5. Экспорт группы")
    println("0. Выход")
    println("=".repeat(40))
    print("Выберите действие: ")
}
