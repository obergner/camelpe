<?xml version="1.0" encoding="UTF-8"?>

<env:EventEnvelope
	xmlns:env="http://www.external.com/schema/ns/event/event-envelope/1.0/"
	xmlns:event="http://www.external.com/schema/ns/events/itemcreated/2.0/"
	xmlns:model="http://www.external.com/schema/ns/models/item/2.0/"
	xmlns:v="http://www.external.com/schema/ns/schema-versioning/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" v:version="1.0">
	<env:Headers>
		<env:EventType>urn:event-type:item.Created</env:EventType>
		<env:EventID>${itemCreatedEvent.headers.eventId}</env:EventID>
		<env:CreationTimestamp>2001-12-31T12:00:00</env:CreationTimestamp>
	</env:Headers>
	<env:Payload>
		<event:CreatedItem ItemNumber="${itemCreatedEvent.createdItem.itemNumber}"
			v:version="2.0">
			<model:Name>${itemCreatedEvent.createdItem.name}</model:Name>
			<model:Description>${itemCreatedEvent.createdItem.description}</model:Description>
			<model:ItemSpecifications>
				<#list itemCreatedEvent.createdItem.specifications as spec>
				<model:ItemSpecification ItemSpecificationNumber="${spec.itemSpecificationNumber}">
					<model:Name>${spec.name}</model:Name>
				</model:ItemSpecification>
				</#list>
			</model:ItemSpecifications>
		</event:CreatedItem>
	</env:Payload>
</env:EventEnvelope>
