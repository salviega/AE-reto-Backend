package co.com.sofka.questions.routers;

import co.com.sofka.questions.body_interfaces_swagger.AnswerBody;
import co.com.sofka.questions.body_interfaces_swagger.QuestionBody;
import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.usecases.AddAnswerUseCase;
import co.com.sofka.questions.usecases.CreateUseCase;
import co.com.sofka.questions.usecases.DeleteUseCase;
import co.com.sofka.questions.usecases.GetUseCase;
import co.com.sofka.questions.usecases.ListUseCase;
import co.com.sofka.questions.usecases.OwnerListUseCase;
import co.com.sofka.questions.usecases.UpdateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class QuestionRouter {

    @Bean
    @RouterOperation(operation = @Operation(operationId = "getAllQuestions", summary = "Get all questions", tags = "Questions",
            responses = {@ApiResponse(responseCode = "200", description = "Successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionBody.class))
            })}
    ))
    public RouterFunction<ServerResponse> getAllQuestions(ListUseCase listUseCase) {
        return route(GET("/getAllQuestions"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(listUseCase.get(), QuestionDTO.class))
        );
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "getAllQuestionsUserId", summary = "Get all questions by userId",
            tags = "Questions",
            responses = {@ApiResponse(responseCode = "200", description = "Successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionDTO.class))
            })},
            parameters = {@Parameter(in = ParameterIn.PATH, name = "userId",
                    description = "User Id",
                    required = true)}
    ))
    public RouterFunction<ServerResponse> getAllQuestionsByUserId(OwnerListUseCase ownerListUseCase) {
        return route(
                GET("/getAllQuestions/{userId}"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                ownerListUseCase.apply(request.pathVariable("userId")),
                                QuestionDTO.class
                        ))
        );
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "create", summary = "Create a question", tags = "Questions",
            responses = {@ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionBody.class))
            })},
            requestBody = @RequestBody(required = true, description = "Insert a Question",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionBody.class))
                    })
    ))
    public RouterFunction<ServerResponse> createQuestion(CreateUseCase createUseCase) {
        Function<QuestionDTO, Mono<ServerResponse>> executor = questionDTO -> createUseCase.apply(questionDTO)
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(result));

        return route(
                POST("/createQuestion").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(QuestionDTO.class).flatMap(executor)
        );
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "Get", summary = "Get a question", tags = "Questions",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id",
                            description = "Question Id",
                            required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDTO.class))
                    })}
    ))
    public RouterFunction<ServerResponse> getQuestion(GetUseCase getUseCase) {
        return route(
                GET("/getQuestion/{id}" ).and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getUseCase.apply(
                                        request.pathVariable("id")),
                                QuestionDTO.class
                        ))
        );
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "addAnswer", summary = "Add an answer", tags = "Answers",
            requestBody = @RequestBody(required = true, description = "Insert an Answer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerBody.class))
                    }),
            responses = {@ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AnswerDTO.class))
            })}
    ))
    public RouterFunction<ServerResponse> addAnswer(AddAnswerUseCase addAnswerUseCase) {
        return route(POST("/addAnswer").and(accept(MediaType.APPLICATION_JSON)),
                request ->
                        request.bodyToMono(AnswerDTO.class)
                                .flatMap(addAnswerDTO -> addAnswerUseCase.apply(addAnswerDTO)
                                        .flatMap(result -> ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(result))
                                )

        );
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "updateQuestion", summary = "Update a question", tags = "Questions",

            requestBody = @RequestBody(required = true, description = "Insert a Question",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionBody.class))
                    }),
            responses = {@ApiResponse(responseCode = "200", description = "Successfully updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionDTO.class))
            })}
    ))
    public RouterFunction<ServerResponse> updateQuestion(UpdateUseCase updateUseCase) {
        return route(
                PUT("/updateQuestion").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(QuestionDTO.class)
                        .flatMap(questionUpdate -> updateUseCase.apply(questionUpdate)
                                .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result)
                                )
                        )
        );
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "updateAnswer", summary = "Update an answer", tags = "Answers",

            requestBody = @RequestBody(required = true, description = "Insert an Answer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerBody.class))
                    }),
            responses = {@ApiResponse(responseCode = "200", description = "Successfully updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AnswerDTO.class))
            })}
    ))
    public RouterFunction<ServerResponse> updateAnswer(UpdateUseCase updateUseCase) {
        return route(
                PUT("/updateAnswer").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AnswerDTO.class)
                        .flatMap(updateUseCase::updateAnswer)
                        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
        );
    }


    @Bean
    @RouterOperation(operation = @Operation(operationId = "deleteQuestionById", summary = "Remove a question by its id",
            tags = "Questions",
            responses = {@ApiResponse(responseCode = "200", description = "Question removed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionDTO.class))
            })},
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id",
                    description = "question id",
                    required = true)}
    ))
    public RouterFunction<ServerResponse> deleteQuestionById(DeleteUseCase deleteUseCase) {
        return route(
                DELETE("/deleteQuestion/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(deleteUseCase.apply(request.pathVariable("id")), Void.class))
        );
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "deleteAnswerById", summary = "Remove an answer by its id",
            tags = "Answers",
            responses = {@ApiResponse(responseCode = "200", description = "Answer removed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AnswerDTO.class))
            })},
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id",
                    description = "answer id",
                    required = true)}
    ))
    public RouterFunction<ServerResponse> deleteAnswerById(DeleteUseCase deleteUseCase) {
        return route(
                DELETE("/deleteAnswer/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(deleteUseCase
                                .deleteAnswerById(request.pathVariable("id")), Void.class))
        );
    }
}


