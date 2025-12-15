import java.io.File

class FileHandler {
    private val contactsFile = "contacts.txt"
    private val groupsFile = "groups.txt"

    // Загрузка контактов
    fun zagruzitKontakty(): MutableList<Contact> {
        val kontakty = mutableListOf<Contact>()
        val fayl = File(contactsFile)

        if (!fayl.exists()) return kontakty

        fayl.forEachLine { stroka ->
            val kontakt = parseKontakt(stroka)
            if (kontakt != null) kontakty.add(kontakt)
        }

        return kontakty
    }

    private fun parseKontakt(stroka: String): Contact? {
        if (stroka.isBlank()) return null

        val chasti = stroka.split("|")
        if (chasti.size < 5) return null

        return try {
            Contact(
                id = chasti[0].toInt(),
                name = chasti[1],
                phone = chasti[2],
                email = chasti[3],
                group = chasti[4],
                notes = if (chasti.size > 5) chasti[5] else ""
            )
        } catch (e: Exception) {
            null
        }
    }

    // Сохранение контактов
    fun sohranitKontakty(kontakty: List<Contact>) {
        val soderzhanie = kontakty.joinToString("\n") { kontakt ->
            "${kontakt.id}|${kontakt.name}|${kontakt.phone}|${kontakt.email}|${kontakt.group}|${kontakt.notes}"
        }
        File(contactsFile).writeText(soderzhanie)
    }

    // Загрузка групп
    fun zagruzitGruppy(): MutableList<ContactGroup> {
        val gruppy = mutableListOf<ContactGroup>()
        val fayl = File(groupsFile)

        if (!fayl.exists()) {
            val defaultGruppy = listOf(
                ContactGroup("Друзья", "Личные контакты"),
                ContactGroup("Работа", "Рабочие контакты"),
                ContactGroup("Семья", "Семейные контакты")
            )
            sohranitGruppy(defaultGruppy)
            return defaultGruppy.toMutableList()
        }

        fayl.forEachLine { stroka ->
            val gruppa = parseGruppu(stroka)
            if (gruppa != null) gruppy.add(gruppa)
        }

        return gruppy
    }

    private fun parseGruppu(stroka: String): ContactGroup? {
        if (stroka.isBlank()) return null

        val chasti = stroka.split("|")
        if (chasti.size < 2) return null

        return ContactGroup(
            name = chasti[0],
            description = chasti[1]
        )
    }

    // Сохранение групп
    fun sohranitGruppy(gruppy: List<ContactGroup>) {
        val soderzhanie = gruppy.joinToString("\n") { gruppa ->
            "${gruppa.name}|${gruppa.description}"
        }
        File(groupsFile).writeText(soderzhanie)
    }

    // Экспорт группы в отдельный файл
    fun eksportirovaTGruppu(nazvanie: String, kontakty: List<Contact>) {
        val imyaFayla = "export_${nazvanie.replace(" ", "_")}.txt"
        val soderzhanie = kontakty.joinToString("\n") { kontakt ->
            "${kontakt.id}|${kontakt.name}|${kontakt.phone}|${kontakt.email}|${kontakt.group}|${kontakt.notes}"
        }
        File(imyaFayla).writeText(soderzhanie)
        println("Группа '$nazvanie' экспортирована в файл: $imyaFayla")
    }
}
