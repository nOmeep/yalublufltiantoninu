package input

import java.io.File
import java.io.FileNotFoundException

/**
 * Класс для чтения пользвовательских параметров из файла syntax.txt
 */
class SettingsReader {
    private val nameToValueMap = mutableMapOf(
        "NStart" to DEFAULT_NON_TERMINAL_START,
        "NEnd" to DEFAULT_NON_TERMINAL_END,
        "Arrow" to DEFAULT_ARROW,
        "Epsilon" to DEFAULT_EPSILON,
        "IterStart" to DEFAULT_ITER_START,
        "IterEnd" to DEFAULT_ITER_END,
        "OptionalEnterStart" to DEFAULT_OPTIONAL_ENTER_START,
        "OptionalEnterEnd" to DEFAULT_OPTIONAL_ENTER_END,
        "Alternative" to DEFAULT_ALTERNATIVE
    )

    /**
     * Возвращает параметры, прочитанные из файла syntax.txt;
     * если что-то пошло не так, то отдает дефолтные параметры
     */
    fun readSyntax(): MutableMap<String, String> {
        safeReadSyntax()
        return nameToValueMap
    }

    private fun safeReadSyntax() {
        try {
            File(DEFAULT_SETTINGS_FILE_NAME)
                .forEachLine { line -> readSetting(line) }

            nameToValueMap.forEach {
                println(it)
            }
        } catch (e: FileNotFoundException) {
            println("Не удалось найти файл syntax.txt")
        } catch (e: Exception) {
            println("Ошибка при чтении файла: ${e.localizedMessage}")
        }
    }

    @Throws(IllegalStateException::class)
    private fun readSetting(line: String) {
        val noWhitespaceLine = line.filter { character -> !character.isWhitespace() }

        val (name, value) = try {
            noWhitespaceLine.split(DEFAULT_SEPARATOR)
        } catch (e: Exception) {
            println("Не удалось распарсить строку:\n\t$line")
            return
        }

        nameToValueMap.putIfPresent(name, value)
    }

    private fun <K, V> MutableMap<K, V>.putIfPresent(key: K, value: V) {
        if (containsKey(key)) {
            put(key, value)
        }
    }

    companion object {
        private const val DEFAULT_SETTINGS_FILE_NAME = "src/syntax.txt"
        private const val DEFAULT_SEPARATOR = "="

        private const val DEFAULT_NON_TERMINAL_START = "["
        private const val DEFAULT_NON_TERMINAL_END = "]"
        private const val DEFAULT_ARROW = "::="
        private const val DEFAULT_EPSILON = "e"
        private const val DEFAULT_ITER_START = ""
        private const val DEFAULT_ITER_END = ""
        private const val DEFAULT_OPTIONAL_ENTER_START = "{"
        private const val DEFAULT_OPTIONAL_ENTER_END = "}"
        private const val DEFAULT_ALTERNATIVE = "|"
    }
}