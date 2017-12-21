package com.shellshellfish.aaas.dto;

public class Answer {
	private OptionItem selectedOption;
	
	public Answer() {
		
	}
	
	public Answer(Integer questionOrdinal, OptionItem selectedOption) {
		super();
		this.selectedOption = selectedOption;
	}
	public OptionItem getSelectedOption() {
		return selectedOption;
	}
	public void setSelectedOption(OptionItem selectedOption) {
		this.selectedOption = selectedOption;
	}
	

}
