package io.github.gstfnk.hello;

import static org.junit.Assert.assertEquals;

import io.github.gstfnk.lang.Lang;
import io.github.gstfnk.lang.LangRepository;
import org.junit.Test;

import java.util.Optional;

public class HelloServiceTest {
    private final static String WELCOME = "Hello";
    private final static String FALLBACK_ID_WELCOME = "Hola";

    @Test
    public void test_prepareGreeting_nullName_returnsGreetingWithFallbackName() throws Exception {
        //  given
        var mockRepository = alwaysReturningHelloRepository();
        var SUT = new HelloService(mockRepository);   // SystemUnderTest
        //  when
        var result = SUT.prepareGreeting(null, "-1");
        //  then
        assertEquals(WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    @Test
    public void test_prepareGreeting_name_returnsGreetingWithName() throws Exception {
        //  given
        var SUT = new HelloService();
        String name = "test";
        //  when
        var result = SUT.prepareGreeting(name, "-1");
        //  then
        assertEquals(WELCOME + " " + name + "!", result);
    }

    @Test
    public void test_prepareGreeting_nullLang_returnsGreetingWithFallbackIdLang() throws Exception {
        //  given
        var mockRepository = fallbackLandIgRepository();
        var SUT = new HelloService(mockRepository);
        //  when
        var result = SUT.prepareGreeting(null, null);
        //  then
        assertEquals(FALLBACK_ID_WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    @Test
    public void test_prepareGreeting_nonExistingLang_returnsGreetingWithFallbackLang() throws Exception {
        //  given
        var mockRepository = alwaysReturningEmptyRepository();
        var SUT = new HelloService(mockRepository);
        //  when
        var result = SUT.prepareGreeting(null, "-1");
        //  then
        assertEquals(HelloService.FALLBACK_LANG.getWelcomeMsg() + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    private LangRepository alwaysReturningEmptyRepository() {
        return new LangRepository() {
            @Override
            public Optional<Lang> findById(Integer id) {
                return Optional.empty();
            }
        };
    }

    @Test
    public void test_prepareGreeting_textLang_returnsGreetingWithFallbackIdLang() throws Exception {
        //  given
        var mockRepository = fallbackLandIgRepository();
        var SUT = new HelloService(mockRepository);
        //  when
        var result = SUT.prepareGreeting(null, "abc");
        //  then
        assertEquals(FALLBACK_ID_WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    private LangRepository fallbackLandIgRepository() {
        return new LangRepository() {
            @Override
            public Optional<Lang> findById(Integer id) {
                if (id.equals(HelloService.FALLBACK_LANG.getId())) {
                    return Optional.of(new Lang(null, FALLBACK_ID_WELCOME, null));
                }
                return Optional.empty();
            }
        };
    }

    private LangRepository alwaysReturningHelloRepository() {
        return new LangRepository() {
            @Override
            public Optional<Lang> findById(Integer id) {
                return Optional.of(new Lang(null, WELCOME, null));
            }
        };
    }
}
