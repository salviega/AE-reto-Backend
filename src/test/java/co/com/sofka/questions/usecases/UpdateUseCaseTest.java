package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UpdateUseCaseTest {

    @MockBean
    QuestionRepository questionRepository;
    @SpyBean
    UpdateUseCase updateUseCase;


    @Test
    @DisplayName("Update a question")
    void UpdateQuestion() {

        var question = new Question();
        question.setId("1");
        question.setUserId("xxxx-xxxx");
        question.setType("tech");
        question.setCategory("software");
        question.setQuestion("¿Que es java?");

        when(questionRepository.findById("1"))
                .thenReturn(Mono.just(question));

        when(questionRepository.save(Mockito.any(Question.class))).thenReturn(Mono.just(question));

        var questionDto = new QuestionDTO(
                "1",
                "1234",
                "Tech",
                "Software",
                "¿Que es java?"
        );

        when(updateUseCase.apply(Mockito.any(QuestionDTO.class))).thenReturn(Mono.just(questionDto.getId()));

        var questionUpdated = updateUseCase.apply(questionDto).block();

        assertEquals(questionUpdated, questionDto.getId());

    }

}