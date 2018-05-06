package com.quadrosepaineisapi.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.quadrosepaineisapi.event.ResourceCreatedEvent;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {

	@Override
	public void onApplicationEvent(ResourceCreatedEvent resourceCreatedEvent) {
		HttpServletResponse response = resourceCreatedEvent.getResponse();
		Long id = resourceCreatedEvent.getId();
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(id).toUri();
			response.setHeader("Location", uri.toASCIIString());
		
	}

}
