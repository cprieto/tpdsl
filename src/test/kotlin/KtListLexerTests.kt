import junit.framework.Test
import com.cprieto.learning.tpdsl.kt

class KtListLexerTests {
    @Test
    fun itReturnsEmptyStringAsEOF() {
        val lexer = KtListLexer("")
    }
}