package br.com.codexloja.v1.domain.enums;

public enum Perfil {

    ADMINISTRADOR(0, "ROLE_ADMINISTRADOR"), SOCIO(1,"ROLE_SOCIO"),
    GERENTE_ADMINISTRATIVO(2,"ROLE_GERENTE_ADMINISTRATIVO"),ADMINISTRATIVO(3,"ROLE_ADMINISTRATIVO"),
    GERENTE_VENDAS(4,"ROLE_GERENTE_VENDAS"),VENDAS(5, "ROLE_VENDAS"),
    GERENTE_COMPRAS(6,"ROLE_GERENTE_COMPRAS"), COMPRAS(7,"ROLE_COMPRAS"),
    GERENTE_FINANCEIRO(8,"ROLE_GERENTE_FINANCEIRO"), FINANCEIRO(9,"ROLE_FINANCEIRO"),
    GERENTE_CONTABILIDADE(10, "ROLE_GERENTE_CONTABILIDADE"), CONTABILIDADE(11, "ROLE_CONTABILIDADE"),
    GERENTE_ESTOQUE(12, "ROLE_GERENTE_ESTOQUE"), ESTOQUE(13, "ROLE_ESTOQUE"),
    GERENTE_FATURAMENTO(14, "ROLE_GERENTE_FATURAMENTO"), FATURAMENTO(15, "ROLE_FATURAMENTO"),
    GERENTE_TI(16, "ROLE_GERENTE_TI"), TI(17, "ROLE_TI");

    private final Integer codigo;
    private final String descricao;

    Perfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Perfil toEnum(Integer codigo){

        if(codigo ==null){
            return null;
        }

        for(Perfil p : Perfil.values()){
            if(codigo.equals(p.getCodigo())){
                return p;
            }
        }

        throw new IllegalArgumentException("Perfil Inválido");
    }
}
