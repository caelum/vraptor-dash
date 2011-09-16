package br.com.caelum.vraptor.dash.monitor;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import br.com.caelum.vraptor.view.Status;

@RunWith(MockitoJUnitRunner.class)
public class SystemControllerTest {

	private @Mock Result result;
	private @Mock Status httpStatus;
	private @Mock Environment environment;
	private SystemController controller;

	@Before
	public void setUp() throws Exception {
		controller = new SystemController(environment, result);
		when(result.use(Results.status())).thenReturn(httpStatus);
	}

	@Test
	public void allowsAccessToExistingFilesMatchingTheConfiguredPattern() throws Exception {
		when(environment.get(SystemController.ALLOWED_LOG_REGEX)).thenReturn("src/test/resources/pattern.log");
		InputStreamDownload logContent = controller.log("src/test/resources/pattern.log");
		Assert.assertNotNull(logContent);
	}

	@Test(expected=IllegalStateException.class)
	public void throwsIllegalStateExceptionIfThePatternMatchesTheFilenameAndTheFileDoesNotExists() throws Exception {
		when(environment.get(SystemController.ALLOWED_LOG_REGEX)).thenReturn("does_not_exist.log");
		controller.log("does_not_exist.log");
	}

	@Test
	public void ifTheConfiguredPatternDoesNotMatchTheFilenameSendStatusForbidden() throws Exception {
		when(environment.get(SystemController.ALLOWED_LOG_REGEX)).thenReturn("src/test/resources/pattern.log");
		controller.log("not_a_match.log");
		verify(httpStatus).forbidden(anyString());
	}

}
