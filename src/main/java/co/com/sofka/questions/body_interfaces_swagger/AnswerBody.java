package co.com.sofka.questions.body_interfaces_swagger;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AnswerBody {

    @NotBlank(message = "There must be an id for this answer")
    private String userId;
    @NotBlank
    private String questionId;
    @NotBlank
    private String answer;
    @Max(5)
    @Min(1)
    @NotNull
    private Integer position;
    private String email;

    public AnswerBody(String userId, String questionId, String answer, Integer position, String email) {
        this.userId = userId;
        this.questionId = questionId;
        this.answer = answer;
        this.position = position;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Answer {" +
                "userId='" + userId + '\'' +
                ", questionId='" + questionId + '\'' +
                ", answer='" + answer + '\'' +
                ", position=" + position +
                '}';
    }
}
