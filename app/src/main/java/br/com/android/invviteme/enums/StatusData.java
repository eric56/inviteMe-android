package br.com.android.invviteme.enums;

public enum StatusData {

	ATIVO(1, "Ativo"),
	CANCELADO(2, "Cancelado"),
	BLOQUEADO(3, "Bloqueado"),
	FINALIZADO(4, "Finalizado"),
	PENDENTE(5, "Pendente"),
	DESATIVADO(6, "Desativado");
			
	private Integer id;
	private String description;
	
	StatusData(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}