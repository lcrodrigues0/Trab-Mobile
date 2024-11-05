import java.io.File

enum class TipoRoupa {
    CAMISA, MOLETON, ACESSORIO;

    companion object {
        fun fromString(str: String): TipoRoupa? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}

enum class Tamanho {
    PP, P, M, G, GG, XG, XXG;

    companion object {
        fun fromString(str: String): Tamanho? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}

enum class TipoEletronico {
    VIDEO_GAME, JOGO, PORTATIL, OUTROS;

    companion object {
        fun fromString(str: String): TipoEletronico? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                OUTROS
            }
        }
    }
}

enum class TipoColecionavel {
    LIVRO, BONECO, OUTROS;

    companion object {
        fun fromString(str: String): TipoColecionavel? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                OUTROS
            }
        }
    }
}

enum class MaterialFabricacao {
    PAPEL, PLASTICO, ACO, MISTURADO, OUTROS;

    companion object {
        fun fromString(str: String): MaterialFabricacao? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                OUTROS
            }
        }
    }
}

enum class Relevancia {
    COMUM, MEDIO, RARO, RARISSIMO;

    companion object {
        fun fromString(str: String): Relevancia? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}

open class Produto(val nome: String, val preco_compra: Double, val preco_venda: Double, var cod: String)

class Roupa(
    cod: String,
    nome: String,
    preco_compra: Double,
    preco_venda: Double,
    var cor_primaria: String,
    var cor_secundaria: String? = null
) : Produto(nome, preco_compra, preco_venda, cod) {
    var tipo: TipoRoupa? = null
    var tamanho: Tamanho? = null

    constructor(
        cod: String,
        nome: String,
        preco_compra: Double,
        preco_venda: Double,
        tipo: String,
        tamanho: String,
        cor_primaria: String,
        cor_secundaria: String? = null
    ) : this(cod, nome, preco_compra, preco_venda, cor_primaria, cor_secundaria) {
        this.cod = "R-" + this.cod
        this.tipo = TipoRoupa.fromString(tipo)
        this.tamanho = Tamanho.fromString(tamanho)
    }

    override fun toString(): String {
        var res: String = ""

        res += "Codigo; ${this.cod}, "
        res += "Nome: ${this.nome}, "
        res += "Preço Compra: ${this.preco_compra}, "
        res += "Preço Venda: ${this.preco_venda}, "
        res += "Tipo: ${this.tipo ?: "-"}, "
        res += "Tamanho: ${this.tamanho ?: "-"}, "
        res += "Cor Primária: ${this.cor_primaria}, "
        res += "Cor Secundária: ${this.cor_secundaria ?: "-"}"

        return res
    }
}

class Eletronico(
    cod: String,
    nome: String,
    preco_compra: Double,
    preco_venda: Double,
    val versao: String,
    val ano: Int
) : Produto(nome, preco_compra, preco_venda, cod) {
    var tipo: TipoEletronico? = null

    constructor (
        cod: String,
        nome: String,
        preco_compra: Double,
        preco_venda: Double,
        tipo: String,
        versao: String,
        ano: Int
    ) : this(cod, nome, preco_compra, preco_venda, versao, ano) {
        this.cod = "E-" + this.cod
        this.tipo = TipoEletronico.fromString(tipo)
    }

    override fun toString(): String {
        var res: String = ""

        res += "Código: ${this.cod}, "
        res += "Nome: ${this.nome}, "
        res += "Preço Compra: ${this.preco_compra}, "
        res += "Preço Venda: ${this.preco_venda}, "
        res += "Tipo: ${this.tipo ?: "-"}, "
        res += "Versão: ${this.versao}, "
        res += "Ano: ${this.ano}"

        return res
    }
}

class Colecionavel(
    cod: String,
    nome: String,
    preco_compra: Double,
    preco_venda: Double,
) : Produto(nome, preco_compra, preco_venda, cod) {
    var tipo: TipoColecionavel? = null
    var material: MaterialFabricacao? = null
    var relevancia: Relevancia? = null
    var tamanho: Double? = null

    constructor(
        cod: String,
        nome: String,
        preco_compra: Double,
        preco_venda: Double,
        tipo: String,
        tamanho: String,
        material: String,
        relevancia: String
    ) : this(cod, nome, preco_compra, preco_venda) {
        this.cod = "C-" + this.cod

        this.tipo = TipoColecionavel.fromString(tipo)
        this.material = MaterialFabricacao.fromString(material)
        this.relevancia = Relevancia.fromString(relevancia)

        try {
            this.tamanho = tamanho.toDouble()
        } catch (e: NumberFormatException) {
            // System.err.println("Tamanho informado não é válido")
        }
    }

    override fun toString(): String {
        var res: String = ""

        res += "Código: ${this.cod}, "
        res += "Nome: ${this.nome}, "
        res += "Preço Compra: ${this.preco_compra}, "
        res += "Preço Venda: ${this.preco_venda}, "
        res += "Tipo: ${this.tipo ?: "-"}, "
        res += "Material: ${this.material ?: "-"}, "
        res += "Relevância: ${this.relevancia ?: "-"}, "
        res += "Tamanho: ${this.tamanho ?: "-"}"

        return res
    }
}

fun leEntradas(path: String, produtos_mp: MutableMap<String, Produto>, estoque_mp: MutableMap<String, Int>) {
    var comprasFilePath = path

    File(comprasFilePath).bufferedReader().useLines { lines ->
        for (line in lines.drop(1)) {
            var values = line.split(",")
            values = values.map { it.uppercase() }

            val qtd = values[1].toInt()
            val p: Produto? = when (values[5]) {
                "ROUPA" -> Roupa(
                    values[0],
                    values[2],
                    values[3].toDouble(),
                    values[4].toDouble(),
                    values[6],
                    values[7],
                    values[8]
                )

                "COLECIONAVEL" -> Colecionavel(
                    values[0],
                    values[2],
                    values[3].toDouble(),
                    values[4].toDouble(),
                    values[6],
                    values[7],
                    values[12],
                    values[13]
                )

                "ELETRONICO" -> Eletronico(
                    values[0],
                    values[2],
                    values[3].toDouble(),
                    values[4].toDouble(),
                    values[6],
                    values[10],
                    values[11].toInt(),
                )

                else -> null
            }

            p?.let {
                if (!produtos_mp.containsKey(p.cod)) {
                    produtos_mp[p.cod] = p
                    estoque_mp[p.cod] = qtd
                } else {
                    estoque_mp[p.cod] = qtd + estoque_mp[p.cod]!!
                }
            }
        }
    }
}

//fun geraEstoqueGeral(outPath: String, estoque : MutableMap<String, Int>){
//    val file = File(outPath + "/estoque_geral.csv")
//
//    file.printWriter().use {
//
//    }
//
//}

fun main() {
    var produtos_mp = mutableMapOf<String, Produto>()
    var estoque_mp = mutableMapOf<String, Int>()

    var comprasFilePath = "/home-temp/aluno/Downloads/Trab1-Mobile/Testes/exemplo_1/entrada/compras.csv"

    leEntradas(comprasFilePath, produtos_mp, estoque_mp)

//    geraEstoqueGeral()


    for(valor in produtos_mp.values){
        println(valor)
    }
    for(par in estoque_mp){
        println(par)
    }

}