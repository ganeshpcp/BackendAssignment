package com.statemnt.model;

import org.springframework.stereotype.Component;

@Component
public class StatementModel {
	
	private Long reference;
	private String accountNumber;
	private String description;
	private Float startBalance;
	private Float mutation;
	private Float endBalance;
	
	
	public StatementModel() {
		super();
	}

	
	
	
	 public StatementModel(Long reference, String accountNumber, String description, Float startBalance, Float mutation,
			Float endBalance) {
		super();
		this.reference = reference;
		this.accountNumber = accountNumber;
		this.description = description;
		this.startBalance = startBalance;
		this.mutation = mutation;
		this.endBalance = endBalance;
	}




	@Override
	public String toString() {
		return "StamentModel [reference=" + reference + ", accountNumber=" + accountNumber + ", description="
				+ description + ", startBalance=" + startBalance + ", mutation=" + mutation + ", endBalance="
				+ endBalance + "]";
	}




	public Long getReference() {
		return reference;
	}




	public void setReference(Long reference) {
		this.reference = reference;
	}




	public String getAccountNumber() {
		return accountNumber;
	}




	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public Float getStartBalance() {
		return startBalance;
	}




	public void setStartBalance(Float startBalance) {
		this.startBalance = startBalance;
	}




	public Float getMutation() {
		return mutation;
	}




	public void setMutation(Float mutation) {
		this.mutation = mutation;
	}




	public Float getEndBalance() {
		return endBalance;
	}




	public void setEndBalance(Float endBalance) {
		this.endBalance = endBalance;
	}




	@Override
     public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((reference == null) ? 0 : reference.hashCode());
         return result;
     }

     @Override
     public boolean equals(Object obj) {
         if (this == obj) {
             return true;
         }
         if (obj == null) {
             return false;
         }
         if (!(obj instanceof StatementModel)) {
             return false;
         }
         StatementModel other = (StatementModel) obj;
         if (reference == null) {
             if (other.reference != null) {
                 return false;
             }
         } else if (!reference.equals(other.reference)) {
             return false;
         }
         return true;
     }





 


	
	
}
