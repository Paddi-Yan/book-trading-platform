package com.turing.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月28日 15:42:38
 */
public class ElasticsearchIndex
{

    public static final String BOOK = "book";
    public static final String USER = "user";
    public static final String COMMUNITY = "community";
/*
    {
        "settings": {
        "analysis": {
            "analyzer": {
                "my_analyzer": {
                    "tokenizer": "ik_max_word",
                            "filter": "py"
                }
            },
            "filter": {
                "py": {
                    "type": "pinyin",
                            "keep_full_pinyin": false,
                            "keep_joined_full_pinyin": true,
                            "keep_original": true,
                            "limit_first_letter_length": 16,
                            "remove_duplicated_term": true,
                            "none_chinese_pinyin_tokenize": false
                }
            }
        }
    },
        "mappings":{
        "properties":{
            "name":{
                "type":"text",
                        "analyzer":"my_analyzer",
                        "search_analyzer":"ik_smart"
            }
        }
    }
    }
 */
    public static final String BOOK_INDEX = "{\n" +
            "    \"mappings\":{\n" +
            "        \"properties\": {\n" +
            "            \"summary\": {\n" +
            "                \"type\": \"text\"\n" +
            "            },\n" +
            "            \"id\": {\n" +
            "                \"type\": \"integer\",\n" +
            "                \"index\": false\n" +
            "            },\n" +
            "            \"key\": {\n" +
            "                \"type\": \"text\",\n" +
            "                \"analyzer\": \"ik_max_word\"\n" +
            "            },\n" +
            "            \"title\": {\n" +
            "                \"type\": \"text\",\n" +
            "                \"copy_to\": [\n" +
            "                    \"key\"\n" +
            "                ],\n" +
            "                \"analyzer\": \"ik_max_word\"\n" +
            "            },\n" +
            "            \"cover\": {\n" +
            "                \"type\": \"text\",\n" +
            "                \"index\": false\n" +
            "            },\n" +
            "            \"price\": {\n" +
            "                \"type\": \"double\"\n" +
            "            },\n" +
            "            \"status\": {\n" +
            "                \"type\": \"integer\"\n" +
            "            },\n" +
            "            \"tagId\": {\n" +
            "                \"type\": \"text\",\n" +
            "                \"index\": false\n" +
            "            },\n" +
            "            \"userId\": {\n" +
            "                \"type\": \"integer\",\n" +
            "                \"index\": false\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
    public static final String USER_INDEX = "{\n" +
            "            \"mappings\": {\n" +
            "            \"properties\": {\n" +
            "                \"avatar\": {\n" +
            "                    \"type\": \"text\",\n" +
            "                    \"index\": false\n" +
            "                },\n" +
            "                \"gender\": {\n" +
            "                    \"type\": \"keyword\",\n" +
            "                    \"index\": false\n" +
            "                },\n" +
            "                \"id\": {\n" +
            "                    \"type\": \"keyword\",\n" +
            "                    \"index\": false\n" +
            "                },\n" +
            "                \"key\": {\n" +
            "                    \"type\": \"text\",\n" +
            "                    \"analyzer\": \"optimizeIK\"\n" +
            "                },\n" +
            "                \"mobile\": {\n" +
            "                    \"type\": \"keyword\",\n" +
            "                    \"index\": false\n" +
            "                },\n" +
            "                \"nickname\": {\n" +
            "                    \"type\": \"text\",\n" +
            "                    \"copy_to\": [\n" +
            "                        \"key\"\n" +
            "                    ],\n" +
            "                    \"analyzer\": \"optimizeIK\"\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"settings\": {\n" +
            "            \"analysis\": {\n" +
            "                \"analyzer\": {\n" +
            "                    \"optimizeIK\": {\n" +
            "                    \"type\": \"custom\",\n" +
            "                    \"tokenizer\": \"ik_max_word\",\n" +
            "                    \"filter\": [\n" +
            "                        \"stemmer\"\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "   }\n" +
            "}";

}
