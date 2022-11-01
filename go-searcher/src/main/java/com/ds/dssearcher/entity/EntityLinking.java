package com.ds.dssearcher.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "entitylinking_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityLinking{
    @Id
    @Field("_id")
    private String id;
    private String docId;
    @Field("responseEntityLinking")
    private ResponseEntityLinking responseEntityLinking;

    @Field("responseEntityLinkingSummary")
    private ResponseEntityLinkingSummary responseEntityLinkingSummary;

    @Document(collection = "responseEntityLinking")
    @Getter
    @Setter
    public static class ResponseEntityLinking{
        private String timestamp;
        @Field("result")
        private Result result;
        @Getter
        @Setter
        @Document(collection = "result")
        public static class Result{
            @Field("entities")
            private List<Entities> entities;
            @Getter
            @Setter
            @Document(collection = "entities")
            public static class Entities{
                @Field("keyword")
                private Keyword keyword;
                @Getter
                @Setter
                @Document(collection = "keyword")
                public static class Keyword{
                    private String word;
                    private String lemma;
                    @Field("span")
                    private Span span;
                    @Field("word_id")
                    private Integer wordId;
                    private Integer status;
                    private String ner;
                    @Field("pos")
                    private Pos pos;
                    @Getter
                    @Setter
                    @Document(collection = "span")
                    public class Span{
                        private Integer start;
                        private Integer end;

                    }

                    @Getter
                    @Setter
                    @Document(collection = "pos")
                    public class Pos{
                        private List<String> universal;
                        private List<String> treebank;
                    }
                }
                @Field("candidate")
                private List<Candidate> candidates;

                @Getter
                @Setter
                @Document(collection = "candidate")
                public static class Candidate{
                    @Field("entity_title")
                    private String entityTitle;
                    @Field("entity_id")
                    private String entityId;
                    private Float score;
                    @Field("meta")
                    private Meta meta;
                    @Getter
                    @Setter
                    @Document(collection = "meta")
                    public class Meta{
                        @Field("cdn_img_url")
                        private String cdnImgUrl;
                        @Field("description")
                        private String description;
                        @Field("stock")
                        private List<Stock> stocks;

                        @Getter
                        @Setter
                        @Document(collection = "stock")
                        public class Stock{
                            @Field("stock_code")
                            private String stockCode;
                            @Field("isin")
                            private String isin;
                            @Field("market")
                            private String market;
                        }
                    }
                }

            }
        }
    }

    @Document(collection = "responseEntityLinkingSummary")
    @Getter
    @Setter
    public static class ResponseEntityLinkingSummary{
        private String timestamp;
        @Field("result")
        private Result result;
        @Getter
        @Setter
        @Document(collection = "result")
        public static class Result{
            @Field("entities")
            private List<Entities> entities;
            @Getter
            @Setter
            @Document(collection = "entities")
            public static class Entities{
                @Field("keyword")
                private Keyword keyword;
                @Getter
                @Setter
                @Document(collection = "keyword")
                public static class Keyword{
                    private String word;
                    private String lemma;
                    @Field("span")
                    private Span span;
                    @Field("word_id")
                    private Integer wordId;
                    private Integer status;
                    private String ner;
                    @Field("pos")
                    private Pos pos;
                    @Getter
                    @Setter
                    @Document(collection = "span")
                    public class Span{
                        private Integer start;
                        private Integer end;

                    }

                    @Getter
                    @Setter
                    @Document(collection = "pos")
                    public static class Pos{
                        private List<String> universal;
                        private List<String> treebank;
                    }
                }
                @Field("candidate")
                private List<Candidate> candidates;

                @Getter
                @Setter
                @Document(collection = "candidate")
                public static class Candidate{
                    @Field("entity_title")
                    private String entityTitle;
                    @Field("entity_id")
                    private String entityId;
                    private Float score;
                    @Field("meta")
                    private Meta meta;
                    @Getter
                    @Setter
                    @Document(collection = "meta")
                    public static class Meta{
                        @Field("cdn_img_url")
                        private String cdnImgUrl;
                        @Field("description")
                        private String description;
                        @Field("stock")
                        private List<Stock> stocks;

                        @Getter
                        @Setter
                        @Document(collection = "stock")
                        public static class Stock{
                            @Field("stock_code")
                            private String stockCode;
                            @Field("isin")
                            private String isin;
                            @Field("market")
                            private String market;
                        }
                    }
                }

            }
        }
    }

}
