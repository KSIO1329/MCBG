package me.ksio.mcbg.commands;

public class VerifiedNode {
	private Node node;
	private Validation Validation;
	public VerifiedNode(Node node, Validation valid){
		this.node = node;
		Validation = valid;
	}
	public Node getNode(){
		return node;
	}
	public void setValdiation(Validation validation){
		Validation = validation;
	}
	public Validation getValidation(){
		return Validation;
	}
}
