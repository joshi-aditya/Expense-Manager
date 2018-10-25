package com.cloud.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "attachment")
public class Attachment{

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", columnDefinition = "BINARY(16)")
	private UUID id;
	
	@Column(name = "uri")
	private String uri;
	
	@ManyToOne
	@JoinColumn(name="transaction_id", nullable=false)
	@JsonIgnore
	private Transaction transaction;

	public Attachment()
	{
		
	}
	
	public Attachment(String uri, Transaction transaction)
	{
		this.uri = uri;
		this.transaction = transaction;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
