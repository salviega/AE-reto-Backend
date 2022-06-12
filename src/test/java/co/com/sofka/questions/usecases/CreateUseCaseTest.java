package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CreateUseCaseTest {

    @MockBean
    QuestionRepository repository;
    @SpyBean
    CreateUseCase createUseCase;


    @Test
    @DisplayName("Save a question")
    void saveAQuestion() {
        var question = new Question();
        question.setId("1");
        question.setUserId("xxxx-xxxx");
        question.setType("tech");
        question.setCategory("software");
        question.setQuestion("¿Que es java?");

        var questionDTO = new QuestionDTO();
        questionDTO.setId("1");
        questionDTO.setUserId("xxxx-xxxx");
        questionDTO.setType("tech");
        questionDTO.setCategory("software");
        questionDTO.setQuestion("¿Que es java?");

        when(repository.save(Mockito.any(Question.class))).thenReturn(Mono.just(question));

        var questionToSave = createUseCase.apply(questionDTO).block();

        assertEquals(questionToSave, question.getId());
    }
}