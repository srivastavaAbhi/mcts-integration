package org.motechproject.mcts.integration.service;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MCTSHttpClientServiceTest {

	@Mock
	private PropertyReader propertyReader;
	
	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private MCTSHttpClientService mctsHttpClientService;

	@Before
	public void setUp() throws Exception {
		mctsHttpClientService = new MCTSHttpClientService(restTemplate,
				propertyReader);
	}
	
	@Test
	public void shouldSyncBeneficiariesToMCTS() {
		String requestUrl = "requestUrl";
		BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
		when(propertyReader.getUpdateRequestUrl()).thenReturn(
				requestUrl);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(beneficiaryRequest, httpHeaders);
		mctsHttpClientService.syncTo(beneficiaryRequest);
		verify(restTemplate).postForEntity(requestUrl, httpEntity,
				String.class);
	}

	@Test
	public void shouldSyncBeneficiariesFromMCTS() {
		String requestUrl = "requestUrl";
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity expectedRequestEntity = new HttpEntity(requestBody,
				httpHeaders);
		when(propertyReader.getBeneficiaryListRequestUrl())
				.thenReturn(requestUrl);
		String expectedResponse = "ResponseBody";
		when(
				restTemplate.exchange(requestUrl, HttpMethod.POST,
						expectedRequestEntity, String.class)).thenReturn(
				new ResponseEntity<>(expectedResponse, HttpStatus.OK));

		String actualResponse = mctsHttpClientService.syncFrom(requestBody);

		assertEquals(expectedResponse, actualResponse);
	}
}
