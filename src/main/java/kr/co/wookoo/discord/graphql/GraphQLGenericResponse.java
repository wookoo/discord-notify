package kr.co.wookoo.discord.graphql;

import lombok.Getter;

@Getter
public class GraphQLGenericResponse<T> {
    private T data;
}
