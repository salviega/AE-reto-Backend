package co.com.sofka.questions.routers;

import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.usecases.AddAnswerUseCase;
import co.com.sofka.questions.usecases.CreateUseCase;
import co.com.sofka.questions.usecases.DeleteUseCase;
import co.com.sofka.questions.usecases.GetUseCase;
import co.com.sofka.questions.usecases.ListUseCase;
import co.com.sofka.questions.usecases.OwnerListUseCase;
import co.com.sofka.questions.usecases.UpdateUseCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
class QuestionRouterTest {

    @Autowired
    public QuestionRouter router;

    @MockBean
    public ListUseCase listService;
    @MockBean
    public OwnerListUseCase ownerListUseCase;
    @MockBean
    public CreateUseCase createUseCase;
    @MockBean
    public GetUseCase getUseCase;
    @MockBean
    public AddAnswerUseCase addAnswerUseCase;
    @MockBean
    public DeleteUseCase deleteUseCase;
    @MockBean
    public UpdateUseCase updateUseCase;

    @Test
    void getAllQuestions() {
        //Arrange
        Flux<QuestionDTO> questionDTOFlux = Flux.just(new QuestionDTO(), new QuestionDTO());
        given(listService.get()).willReturn(questionDTOFlux);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.getAllQuestions(listService)).build();
        //Assert
        client.get().uri("/getAllQuestions").exchange().expectStatus().isOk()
                .returnResult(QuestionDTO.class).getResponseBody().subscribe();
    }

    @Test
    void getAllQuestionsByUserId() {
        //Arrange
        Flux<QuestionDTO> questionDTOFlux = Flux.just(new QuestionDTO());
        String userId = "1";
        given(ownerListUseCase.apply(userId)).willReturn(questionDTOFlux);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.getAllQuestionsByUserId(ownerListUseCase)).build();
        //Assert
        client.get().uri("/getAllQuestions/{userId}", 1).exchange().expectStatus().isOk()
                .returnResult(QuestionDTO.class).getResponseBody().subscribe();
    }

    @Test
    void createQuestion() {
        //Arrange
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId("1");
        Mono<String> questionId = Mono.just("1");
        given(createUseCase.apply(questionDTO)).willReturn(questionId);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.createQuestion(createUseCase)).build();
        //Assert
        client.post().uri("/createQuestion").exchange().expectStatus().isOk()
                .returnResult(QuestionDTO.class).getResponseBody().subscribe();
    }


    @Test
    void getQuestion() {
        //Arrange
        Mono<QuestionDTO> questionDTOMono = Mono.just(new QuestionDTO());
        String userId = "1";
        given(getUseCase.apply(userId)).willReturn(questionDTOMono);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.getQuestion(getUseCase)).build();
        //Assert
        client.get().uri("/getQuestion/{id}", 1).exchange().expectStatus().isOk()
                .returnResult(QuestionDTO.class).getResponseBody().subscribe();
    }

    @Test
    void addAnswer() {
        //Arrange
        AnswerDTO answerDTOMono = new AnswerDTO();
        Mono<QuestionDTO> questionDTOMono = Mono.just(new QuestionDTO());
        given(addAnswerUseCase.apply(answerDTOMono)).willReturn(questionDTOMono);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.addAnswer(addAnswerUseCase)).build();
        //Assert
        client.post().uri("/addAnswer").exchange().expectStatus().isOk()
                .returnResult(QuestionDTO.class).getResponseBody().subscribe();
    }

    @Test
    void updateQuestion() {
        //Arrange
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId("1");
        Mono<String> stringMono = Mono.just("1");
        given(updateUseCase.apply(questionDTO)).willReturn(stringMono);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.updateQuestion(updateUseCase)).build();
        //Assert
        client.put().uri("/updateQuestion").exchange().expectStatus().isOk()
                .returnResult(QuestionDTO.class).getResponseBody().subscribe();
    }

    @Test
    void deleteQuestionById() {
        //Arrange
        String questionId = "1";
        Mono<Void> monoVoid = Mono.empty();
        given(deleteUseCase.apply(questionId)).willReturn(monoVoid);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.deleteQuestionById(deleteUseCase)).build();
        //Assert
        client.delete().uri("/deleteQuestion/{id}", 1).exchange().expectStatus().isAccepted()
                .returnResult(Void.class).getResponseBody().subscribe();
    }

    @Test
    void deleteAnswerById() {
        //Arrange
        String answerId = "1";
        Mono<Void> voidMono = Mono.empty();
        given(deleteUseCase.deleteAnswerById(answerId)).willReturn(voidMono);
        //Act
        WebTestClient client = WebTestClient.bindToRouterFunction(router.deleteAnswerById(deleteUseCase)).build();
        //Assert
        client.delete().uri("/deleteAnswer/{id}", 1).exchange().expectStatus().isAccepted()
                .returnResult(Void.class).getResponseBody().subscribe();
    }
}