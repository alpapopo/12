class ContactManager {
    private val kontakty = mutableListOf<Contact>()
    private val gruppy = mutableListOf<ContactGroup>()
    private val fileHandler = FileHandler()
    private var sleduyushchiyId = 1

    init {
        zagruzitDannye()
    }

    private fun zagruzitDannye() {
        kontakty.addAll(fileHandler.zagruzitKontakty())
        gruppy.addAll(fileHandler.zagruzitGruppy())

        kontakty.forEach {
            if (it.id >= sleduyushchiyId) sleduyushchiyId = it.id + 1
        }
    }

    // 1. Показать все контакты
    fun pokazatVseKontakty() {
        if (kontakty.isEmpty()) {
            println("\nНет контактов.")
            return
        }

        println("\n=== ВСЕ КОНТАКТЫ (${kontakty.size}) ===")
        kontakty.forEach { vyvestiKontakt(it) }
    }

    private fun vyvestiKontakt(kontakt: Contact) {
        println("\n[ID: ${kontakt.id}]")
        println("Имя: ${kontakt.name}")
        println("Телефон: ${kontakt.phone}")
        println("Email: ${kontakt.email}")
        println("Группа: ${kontakt.group}")
        if (kontakt.notes.isNotEmpty()) {
            println("Заметки: ${kontakt.notes}")
        }
        println("-".repeat(50))
    }

    // 2. Добавить контакт
    fun dobavitKontakt() {
        println("\n=== ДОБАВЛЕНИЕ КОНТАКТА ===")

        print("Введите имя: ")
        val imya = readLine() ?: ""

        val telefon = vvestiTelefon()
        val email = vvestiEmail()

        println("\nДоступные группы:")
        gruppy.forEachIndexed { index, gruppa ->
            println("${index + 1}. ${gruppa.name} - ${gruppa.description}")
        }

        print("Выберите номер группы: ")
        val nomerGruppy = readLine()?.toIntOrNull() ?: 1
        val nazvanijeGruppy = if (nomerGruppy in 1..gruppy.size) {
            gruppy[nomerGruppy - 1].name
        } else {
            gruppy.firstOrNull()?.name ?: "Общее"
        }

        print("Введите заметки (необязательно): ")
        val zametki = readLine() ?: ""

        val kontakt = Contact(
            id = sleduyushchiyId++,
            name = imya,
            phone = telefon,
            email = email,
            group = nazvanijeGruppy,
            notes = zametki
        )

        kontakty.add(kontakt)
        fileHandler.sohranitKontakty(kontakty)
        println("\nКонтакт успешно добавлен! ID: ${kontakt.id}")
    }

    private fun vvestiTelefon(): String {
        while (true) {
            print("Введите телефон: ")
            val telefon = readLine() ?: ""

            if (isValidPhone(telefon)) {
                return telefon
            } else {
                println("Неверный формат телефона. Попробуйте еще раз.")
            }
        }
    }

    private fun vvestiEmail(): String {
        while (true) {
            print("Введите email: ")
            val email = readLine() ?: ""

            if (isValidEmail(email)) {
                return email
            } else {
                println("Неверный формат email. Попробуйте еще раз.")
            }
        }
    }

    // 3. Поиск контакта
    fun poiskKontakta() {
        println("\n=== ПОИСК КОНТАКТОВ ===")
        println("1. По имени")
        println("2. По телефону")
        println("3. По группе")
        print("Выберите критерий поиска: ")

        val rezultat = when (readLine()) {
            "1" -> poiskPoImeni()
            "2" -> poiskPoTelefonu()
            "3" -> poiskPoGruppe()
            else -> {
                println("Неверный выбор.")
                return
            }
        }

        pokazatRezultaty(rezultat)
    }

    private fun poiskPoImeni(): List<Contact> {
        print("Введите имя (или часть): ")
        val zapros = readLine()?.lowercase() ?: ""
        return kontakty.filter { it.name.lowercase().contains(zapros) }
    }

    private fun poiskPoTelefonu(): List<Contact> {
        print("Введите телефон (или часть): ")
        val zapros = readLine() ?: ""
        return kontakty.filter { it.phone.contains(zapros) }
    }

    private fun poiskPoGruppe(): List<Contact> {
        print("Введите название группы: ")
        val nazvanie = readLine() ?: ""
        return kontakty.filter { it.group.equals(nazvanie, ignoreCase = true) }
    }

    private fun pokazatRezultaty(naydennye: List<Contact>) {
        if (naydennye.isEmpty()) {
            println("\nКонтакты не найдены.")
        } else {
            println("\nНайдено контактов: ${naydennye.size}")
            naydennye.forEach { vyvestiKontakt(it) }

            println("\nДействия с результатами:")
            println("1. Редактировать контакт")
            println("2. Удалить контакт")
            println("0. Назад")
            print("Выбор: ")

            when (readLine()) {
                "1" -> redaktirovatKontakt()
                "2" -> udalitKontakt()
            }
        }
    }

    // Редактирование контакта
    private fun redaktirovatKontakt() {
        print("\nВведите ID контакта для редактирования: ")
        val id = readLine()?.toIntOrNull() ?: return

        val indeks = kontakty.indexOfFirst { it.id == id }
        if (indeks == -1) {
            println("Контакт не найден.")
            return
        }

        val staryyKontakt = kontakty[indeks]

        print("Новое имя (Enter - оставить '${staryyKontakt.name}'): ")
        val imya = readLine()?.takeIf { it.isNotEmpty() } ?: staryyKontakt.name

        print("Новый телефон (Enter - оставить '${staryyKontakt.phone}'): ")
        val telefonVvod = readLine()
        val telefon = if (telefonVvod.isNullOrEmpty()) {
            staryyKontakt.phone
        } else if (isValidPhone(telefonVvod)) {
            telefonVvod
        } else {
            println("Неверный формат телефона. Оставлен старый.")
            staryyKontakt.phone
        }

        print("Новый email (Enter - оставить '${staryyKontakt.email}'): ")
        val emailVvod = readLine()
        val email = if (emailVvod.isNullOrEmpty()) {
            staryyKontakt.email
        } else if (isValidEmail(emailVvod)) {
            emailVvod
        } else {
            println("Неверный формат email. Оставлен старый.")
            staryyKontakt.email
        }

        print("Новые заметки (Enter - оставить '${staryyKontakt.notes}'): ")
        val zametki = readLine()?.takeIf { it.isNotEmpty() } ?: staryyKontakt.notes

        kontakty[indeks] = staryyKontakt.copy(
            name = imya,
            phone = telefon,
            email = email,
            notes = zametki
        )

        fileHandler.sohranitKontakty(kontakty)
        println("Контакт успешно обновлен!")
    }

    // Удаление контакта
    private fun udalitKontakt() {
        print("\nВведите ID контакта для удаления: ")
        val id = readLine()?.toIntOrNull() ?: return

        val udaleno = kontakty.removeIf { it.id == id }
        if (udaleno) {
            fileHandler.sohranitKontakty(kontakty)
            println("Контакт ID $id успешно удален!")
        } else {
            println("Контакт не найден.")
        }
    }

    // 4. Управление группами
    fun upravlenieGruppami() {
        while (true) {
            println("\n=== УПРАВЛЕНИЕ ГРУППАМИ ===")
            println("1. Показать все группы")
            println("2. Добавить группу")
            println("3. Удалить группу")
            println("0. Назад")
            print("Выбор: ")

            when (readLine()) {
                "1" -> pokazatGruppy()
                "2" -> dobavitGruppu()
                "3" -> udalitGruppu()
                "0" -> return
                else -> println("Неверный выбор.")
            }
        }
    }

    private fun pokazatGruppy() {
        if (gruppy.isEmpty()) {
            println("\nНет групп.")
            return
        }

        println("\n=== СПИСОК ГРУПП ===")
        gruppy.forEach { gruppa ->
            val kolichestvo = kontakty.count { it.group == gruppa.name }
            println("- ${gruppa.name}: ${gruppa.description} ($kolichestvo контактов)")
        }
    }

    private fun dobavitGruppu() {
        print("\nВведите название группы: ")
        val nazvanie = readLine() ?: ""

        if (gruppy.any { it.name.equals(nazvanie, ignoreCase = true) }) {
            println("Группа с таким названием уже существует.")
            return
        }

        print("Введите описание группы: ")
        val opisanie = readLine() ?: ""

        gruppy.add(ContactGroup(nazvanie, opisanie))
        fileHandler.sohranitGruppy(gruppy)
        println("Группа '$nazvanie' успешно добавлена!")
    }

    private fun udalitGruppu() {
        print("\nВведите название группы для удаления: ")
        val nazvanie = readLine() ?: ""

        val kolichestvo = kontakty.count { it.group.equals(nazvanie, ignoreCase = true) }
        if (kolichestvo > 0) {
            println("В группе '$nazvanie' есть $kolichestvo контактов.")
            print("Все равно удалить? (да/нет): ")
            if (readLine()?.lowercase() != "да") {
                return
            }
        }

        val udaleno = gruppy.removeIf { it.name.equals(nazvanie, ignoreCase = true) }
        if (udaleno) {
            fileHandler.sohranitGruppy(gruppy)
            println("Группа '$nazvanie' удалена!")
        } else {
            println("Группа не найдена.")
        }
    }

    // 5. Экспорт группы
    fun eksportGruppy() {
        print("\nВведите название группы для экспорта: ")
        val nazvanie = readLine() ?: ""

        val kontaktyGruppy = kontakty.filter {
            it.group.equals(nazvanie, ignoreCase = true)
        }

        if (kontaktyGruppy.isEmpty()) {
            println("В группе '$nazvanie' нет контактов.")
            return
        }

        fileHandler.eksportirovaTGruppu(nazvanie, kontaktyGruppy)
        println("Экспортировано контактов: ${kontaktyGruppy.size}")
    }
}
