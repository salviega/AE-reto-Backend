package co.com.sofka.questions.usecases;

import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.reposioties.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class DeleteUseCaseTest {

    @MockBean
    QuestionRepository questionRepository;
    @MockBean
    AnswerRepository answerRepository;
    @SpyBean
    DeleteUseCase deleteUseCase;

    @Test
    @DisplayName("Delete a question by its id")
    void deleteQuestionById() {

        var questionDTO = new QuestionDTO();
        questionDTO.setId("1");
        questionDTO.setUserId("1");
        questionDTO.setType("tech");
        questionDTO.setCategory("software");
        questionDTO.setQuestion("¿Que es java?");

        when(questionRepository.deleteById(Mockito.anyString())).thenReturn(Mono.empty());

        var questionToDelete = deleteUseCase.apply(questionDTO.getId());

        verify(questionRepository).deleteById(questionDTO.getId());

    }

    @Test
    @DisplayName("Delete an answer by its id")
    void deleteAnswerById() {

        when(answerRepository.deleteById(Mockito.anyString())).thenReturn(Mono.empty());

        var answerToDelete = deleteUseCase.deleteAnswerById("1");

        verify(answerRepository).deleteById("1");
    }
}