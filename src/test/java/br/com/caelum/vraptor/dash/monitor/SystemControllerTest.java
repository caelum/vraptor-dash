package br.com.caelum.vraptor.dash.monitor;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import br.com.caelum.vraptor.view.Status;

public class SystemControllerTest {

	@Test
	public void allowsAccessToExistingFilesMatchingTheConfiguredPattern() throws Exception {
		Environment environment = mock(Environment.class);
		when(environment.get(SystemController.ALLOWED_LOG_REGEX)).thenReturn("src/test/resources/pattern.log");
		InputStreamDownload logContent = new SystemController(null, environment, null).log("src/test/resources/pattern.log");
		Assert.assertNotNull(logContent);
	}
	
	@Test(expected=IllegalStateException.class)
	public void throwsIllegalStateExceptionIfThePatternMatchesTheFilenameAndTheFileDoesNotExists() throws Exception {
		Environment environment = mock(Environment.class);
		when(environment.get(SystemController.ALLOWED_LOG_REGEX)).thenReturn("does_not_exist.log");
		new SystemController(null, environment, null).log("does_not_exist.log");
	}
	
	@Test
	public void ifTheConfiguredPatternDoesNotMatchTheFilenameSendStatusForbidden() throws Exception {
		Environment environment = mock(Environment.class);
		Result result = mock(Result.class);
		Status httpStatus = mock(Status.class);
		when(result.use(Results.status())).thenReturn(httpStatus);
		when(environment.get(SystemController.ALLOWED_LOG_REGEX)).thenReturn("src/test/resources/pattern.log");
		new SystemController(null, environment, result).log("not_a_match.log");
		verify(httpStatus).forbidden(anyString());
	}
	
}
