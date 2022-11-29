package integration.com.dmdev.auth;

import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String email() default "test@gmail.com";

    String password() default "testtest";

    String[] authorities() default {"DUMMY"};

    long id() default 1L;

    String username() default "test";

    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;
}