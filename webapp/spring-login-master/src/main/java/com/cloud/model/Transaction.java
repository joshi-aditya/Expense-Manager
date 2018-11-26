package com.cloud.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name = "transaction")
public class Transaction extends Status{

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "transaction_id", columnDefinition = "BINARY(16)")
	private UUID transactionId;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "merchant")
	private String merchant;
	
	@Column(name = "amount")
	private Float amount;
	
	@Column(name = "date")
	private String date;
	
	@Column(name = "category")
	private String category;
	
	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
	@JoinColumn(name = "user_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private User user;
	
	@OneToMany(mappedBy = "transaction")	
	private List<Attachment> attachments;

	public Transaction() {
		attachments = new ArrayList<Attachment>();
	}

	/**
	 * 
	 * @param transactionId
	 * @param description
	 * @param merchant
	 * @param amount
	 * @param date
	 * @param category
	 * @param user
	 * @param attachments
	 */
	public Transaction(String description, String merchant, Float amount, String date, String category,
			User user, List<Attachment> attachments) {
		this.description = description;
		this.merchant = merchant;
		this.amount = amount;
		this.date = date;
		this.category = category;
		this.user = user;
		this.attachments = attachments;
	}

	public UUID getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String string) {
		this.date = string;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachment) {
		this.attachments = attachment;
	}

	
}
