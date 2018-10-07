package com.cloud.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cloud.model.Attachment;



@Repository("attachmentRepository")
public interface AttachmentRepository extends CrudRepository<Attachment, UUID> {

	Attachment findByid(UUID id);

}