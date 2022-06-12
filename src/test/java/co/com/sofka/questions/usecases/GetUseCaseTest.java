package co.com.sofka.questions.usecases;

import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.reposioties.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetUseCaseTest {

    @MockBean
    QuestionRepository questionRepository;
    @MockBean
    AnswerRepository answerRepository;
    @MockBean
    GetUseCase getUseCase;

    @Test
    @DisplayName("Get a question by its id")
    void getQuestionById() {

        var questionDTO = new QuestionDTO();
        questionDTO.setId("1");
        questionDTO.setUserId("xxxx-xxxx");
        questionDTO.setType("tech");
        questionDTO.setCategory("software");
        questionDTO.setQuestion("¿Que es java?");

        when(getUseCase.apply(Mockito.anyString())).thenReturn(Mono.just(questionDTO));

        var questionToGet = getUseCase.apply(questionDTO.getId()).block();

        assert questionToGet != null;
        assertEquals(questionToGet.getId(), questionDTO.getId());
        assertEquals(questionToGet.getQuestion(), questionDTO.getQuestion());
    }
}