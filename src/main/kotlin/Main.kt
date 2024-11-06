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

    fun equalsTo(str: String) : Boolean{
        return str == this.name
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

    fun equalsTo(str: String) : Boolean{
        return str == this.name
    }
}

enum class TipoEletronico {
    VIDEO_GAME, JOGO, PORTATIL, OUTROS;

    companion object {
        fun fromString(str: String): TipoEletronico? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                if(str == "VIDEO-GAME")
                    VIDEO_GAME
                else
                    null
            }
        }
    }

    fun equalsTo(str: String) : Boolean{
        val strModified = str.replace('-', '_')

        return strModified == this.name
    }
}

enum class TipoColecionavel {
    LIVRO, BONECO, OUTROS;

    companion object {
        fun fromString(str: String): TipoColecionavel? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    fun equalsTo(str: String) : Boolean{
        return str == this.name
    }
}

enum class MaterialFabricacao {
    PAPEL, PLASTICO, ACO, MISTURADO, OUTROS;

    companion object {
        fun fromString(str: String): MaterialFabricacao? {
            return try {
                valueOf(str)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    fun equalsTo(str: String) : Boolean{
        return str == this.name
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

    fun equalsTo(str: String) : Boolean{
        return str == this.name
    }
}

open abstract class Produto(val nome: String, val preco_compra: Double, val preco_venda: Double, var cod: String){
    abstract fun atendeAosCriterios(info: InfoBusca): Boolean
}

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

    override fun atendeAosCriterios(info: InfoBusca): Boolean {
        var res = true
        res = res && (info.categoria == "-" || info.categoria == "ROUPA")
        res = res && (info.tipo == "-" || this.tipo?.equalsTo(info.tipo) ?: false)
        res = res && (info.tamanho == "-" || this.tamanho?.equalsTo(info.tamanho) ?: false)
        res = res && (info.cor_primaria == "-" || this.cor_primaria == info.cor_primaria)
        res = res && (info.cor_secundaria == "-" || this.cor_secundaria == info.cor_secundaria)

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

    override fun atendeAosCriterios(info: InfoBusca): Boolean {
        var res = true
        res = res && (info.categoria == "-" || info.categoria == "ELETRONICO")
        res = res && (info.tipo == "-" || this.tipo?.equalsTo(info.tipo) ?: false)
        res = res && (info.versao == "-" || this.versao == info.versao)
        res = res && (info.ano == "-" || this.ano == info.ano.toInt())

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

    override fun atendeAosCriterios(info: InfoBusca): Boolean {
        var res = true
        res = res && (info.categoria == "-" || info.categoria == "COLECIONAVEL")
        res = res && (info.tipo == "-" || this.tipo?.equalsTo(info.tipo) ?: false)
        res = res && (info.material == "-" || this.material?.equalsTo(info.material) ?: false)
        res = res && (info.relevancia == "-" || this.relevancia?.equalsTo(info.relevancia) ?: false)
        res = res && (info.tamanho == "-" || this.tamanho == info.tamanho.toDouble())

        return res
    }
}

data class InfoEstoque(var prod: Produto, var qtd: Int)
data class InfoBalancete(var compras: Double, var vendas: Double, var balancete: Double)

fun leCompras(path: String, estoque_mp: MutableMap<String, InfoEstoque>, balancete_dt: InfoBalancete) {
    var comprasFilePath = path + "/compras.csv"

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
                if (!estoque_mp.containsKey(p.cod)) {
                    estoque_mp[p.cod] = InfoEstoque(p, qtd)
                } else {
                    estoque_mp[p.cod]!!.qtd = qtd + estoque_mp[p.cod]!!.qtd
                }

                balancete_dt.compras += qtd * p.preco_compra
                balancete_dt.balancete -= qtd * p.preco_compra
            }
        }
    }
}

fun leVendas(inPath: String, estoque_mp: MutableMap<String, InfoEstoque>, balancete_dt: InfoBalancete) {
    val vendasFilePath = File(inPath + "/vendas.csv")

    vendasFilePath.bufferedReader().useLines { lines ->
        lines.drop(1).forEach { line ->
            val values = line.split(',')

            val cod = values[0]
            val qtd = values[1].toInt()

            if (estoque_mp.containsKey(cod)) {
                estoque_mp[cod]!!.qtd -= qtd

                balancete_dt.vendas += qtd * estoque_mp[cod]!!.prod.preco_venda
                balancete_dt.balancete += qtd * estoque_mp[cod]!!.prod.preco_venda
            } else {
                println("Produto não presente no estoque")
            }
        }
    }
}

fun geraEstoqueGeral(outPath: String, estoque: MutableMap<String, InfoEstoque>) {
    val file = File(outPath + "/estoque_geral.csv")
    file.createNewFile()

    file.printWriter().use { writer ->
        writer.println("CODIGO,NOME,QUANTIDADE")

        for (value in estoque.values) {
            writer.println("${value.prod.cod},${value.prod.nome},${value.qtd}")
        }
    }
}

fun geraEstoqueCategorias(outPath: String, estoque: MutableMap<String, InfoEstoque>) {
    val file = File(outPath + "/estoque_categorias.csv")

    var qtdRoupa = 0
    var qtdColecionavel = 0
    var qtdEletronico = 0

    for (value in estoque.values) {
        if (value.prod is Roupa)
            qtdRoupa += value.qtd
        else if (value.prod is Colecionavel)
            qtdColecionavel += value.qtd
        else if (value.prod is Eletronico)
            qtdEletronico += value.qtd
    }

    file.createNewFile()
    file.printWriter().use { writer ->
        writer.println("CATEGORIA,QUANTIDADE")
        writer.println("ROUPA,${qtdRoupa}")
        writer.println("COLECIONAVEL,${qtdColecionavel}")
        writer.println("ELETRONICO,${qtdEletronico}")
    }
}

fun geraBalancete(outPath: String, balancete_dt: InfoBalancete) {
    val balanceteFilePath = File(outPath + "/balancete.csv")

    balanceteFilePath.createNewFile()
    balanceteFilePath.printWriter().use { writer ->
        writer.println("COMPRAS,${balancete_dt.compras}")
        writer.println("VENDAS,${balancete_dt.vendas}")
        writer.println("BALANCETE,${balancete_dt.balancete}")
    }
}

data class InfoBusca(
    val categoria: String,
    val tipo: String,
    val tamanho: String,
    val cor_primaria: String,
    val cor_secundaria: String,
    val versao: String,
    val ano: String,
    val material: String,
    val relevancia: String
)

fun buscaQuantidadeNoEstoque(info_dt: InfoBusca, estoque_mp: MutableMap<String, InfoEstoque>): Int {

    return estoque_mp.values.filter{ it.prod.atendeAosCriterios(info_dt) }.sumOf { it.qtd }
}

fun realizaBusca(inPath: String, outPath: String, estoque_mp: MutableMap<String, InfoEstoque>) {
    var qtdBuscas = mutableListOf<Int>()

    val buscasFile = File(inPath + "/busca.csv")

    buscasFile.bufferedReader().useLines { lines ->
        var lineCount = 0

        lines.drop(1).forEach { line ->
            var values = line.split(',')
            values = values.map { it.uppercase() }

            val info_dt = InfoBusca(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
                values[6],
                values[7],
                values[8]
            )

            val qtd = buscaQuantidadeNoEstoque(info_dt, estoque_mp)
            qtdBuscas.add(qtd)
        }
    }

    val resultadoFile = File(outPath + "/resultado_buscas.csv")
    resultadoFile.createNewFile()

    resultadoFile.printWriter().use { writer ->
        writer.println("BUSCAS,QUANTIDADE")

        for((idx, qtd) in qtdBuscas.withIndex()){
            writer.println("${idx + 1},${qtd}")
        }
    }

}

fun main(args: Array<String>) {
    var estoque_mp = mutableMapOf<String, InfoEstoque>()
    var balancete_dt = InfoBalancete(0.0, 0.0, 0.0)
//    var inPath = args[0]
//    var outPath = args[1]

    var inPath = "/home/leticia/Desktop/Trab-Mobile/Testes/exemplo_1/entrada"
    var outPath = "/home/leticia/Desktop/Trab-Mobile/Testes/exemplo_1/minha_saida"

    leCompras(inPath, estoque_mp, balancete_dt)
    leVendas(inPath, estoque_mp, balancete_dt)

    for(value in estoque_mp.values){
        println(value)
    }

    geraEstoqueGeral(outPath, estoque_mp)
    geraEstoqueCategorias(outPath, estoque_mp)
    geraBalancete(outPath, balancete_dt)

    realizaBusca(inPath, outPath, estoque_mp)
}