package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Answer;
import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.reposioties.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class AddAnswerUseCaseTest {

    @MockBean
    QuestionRepository questionRepository;
    @MockBean
    AnswerRepository answerRepository;
    @SpyBean
    AddAnswerUseCase addAnswerUseCase;
    @MockBean
    GetUseCase getUseCase;


    @Test
    @DisplayName("Test for add an answer to a question")
    public void addAnswerUseCase() {

        var questionDTO = new QuestionDTO();
        questionDTO.setId("1");
        questionDTO.setUserId("1");
        questionDTO.setType("tech");
        questionDTO.setCategory("software");
        questionDTO.setQuestion("¿Que es java?");

        AnswerDTO answerDTO = new AnswerDTO("1234",
                "1", "1",
                "answer", 1,
                Instant.now(), Instant.now()
        );
        answerDTO.setEmail("");

        Answer answer = new Answer();
        answer.setId("1234");
        answer.setQuestionId("1");
        answer.setAnswer("answer");
        answer.setPosition(1);
        answer.setCreated(Instant.now());
        answer.setUpdated(Instant.now());

        when(getUseCase.apply(Mockito.anyString())).thenReturn(Mono.just(questionDTO));
        when(answerRepository.save(Mockito.any(Answer.class))).thenReturn(Mono.just(answer));

        var questionFromResponse = addAnswerUseCase.apply(answerDTO).block();

        assertEquals(questionFromResponse != null ? questionFromResponse.getId() : null, questionDTO.getId());
        assert questionFromResponse != null;
        assertEquals(questionFromResponse.getQuestion(), questionDTO.getQuestion());
        assertEquals(questionFromResponse.getUserId(), questionDTO.getUserId());
    }
}