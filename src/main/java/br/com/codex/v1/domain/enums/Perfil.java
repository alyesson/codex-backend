package br.com.codex.v1.domain.enums;

public enum Perfil {

    ADMINISTRADOR(0, "ROLE_ADMINISTRADOR"), SOCIO(1,"ROLE_SOCIO"),
    GERENTE_ADMINISTRATIVO(2, "ROLE_GERENTE_ADMINISTRATIVO"), ADMINISTRATIVO(3, "ROLE_ADMINISTRATIVO"),
    GERENTE_ESTOQUE(4, "ROLE_GERENTE_ESTOQUE"), ESTOQUE(5,"ROLE_ESTOQUE"),
    GERENTE_VENDAS(6, "ROLE_GERENTE_VENDAS"), VENDEDOR(7,"ROLE_VENDEDOR"),
    GERENTE_FATURAMENTO(8, "ROLE_GERENTE_FATURAMENTO"), FATURAMENTO(9, "ROLE_FATURAMENTO"),
    GERENTE_COMPRAS(10,"ROLE_GERENTE_COMPRAS"), COMPRADOR(11,"ROLE_COMPRADOR"),
    GERENTE_FINANCEIRO(12,"ROLE_GERENTE_FINANCEIRO"), FINANCEIRO(13,"ROLE_FINANCEIRO"),
    GERENTE_CONTABILIDADE(14, "ROLE_GERENTE_CONTABILIDADE"), CONTABILIDADE(15, "ROLE_CONTABILIDADE"),
    GERENTE_FISCAL(16, "ROLE_GERENTE_FISCAL"), FISCAL(17, "ROLE_FISCAL"),
    GERENTE_TI(18, "ROLE_GERENTE_TI"), TI(19, "ROLE_TI"),
    GERENTE_RH(20,"ROLE_GERENTE_RH"), RH(21,"ROLE_RH"),
    PCP(22,"ROLE_PCP"), MANUTENCAO(23,"ROLE_MANUTENCAO"), PORTARIA(24,"ROLE_PORTARIA");

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
