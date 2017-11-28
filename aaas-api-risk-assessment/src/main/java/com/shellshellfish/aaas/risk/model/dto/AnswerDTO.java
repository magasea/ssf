package com.shellshellfish.aaas.risk.model.dto;

public class AnswerDTO {
	private Integer questionOrdinal;
	
	private OptionItemDTO selectedOption;
	
	public AnswerDTO() {
		
	}
	
	public AnswerDTO(Integer questionOrdinal, OptionItemDTO selectedOption) {
		super();
		this.questionOrdinal = questionOrdinal;
		this.selectedOption = selectedOption;
	}
	
	public Integer getQuestionOrdinal() {
		return questionOrdinal;
	}
	public void setQuestionOrdinal(Integer questionOrdinal) {
		this.questionOrdinal = questionOrdinal;
	}
	public OptionItemDTO getSelectedOption() {
		return selectedOption;
	}
	public void setSelectedOption(OptionItemDTO selectedOption) {
		this.selectedOption = selectedOption;
	}
	

}
