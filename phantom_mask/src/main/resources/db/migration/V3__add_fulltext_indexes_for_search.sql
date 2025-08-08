ALTER TABLE stores
ADD FULLTEXT INDEX ft_stores_name (name) with PARSER ngram;

ALTER TABLE masks
ADD FULLTEXT INDEX ft_masks_name (name) with PARSER ngram;