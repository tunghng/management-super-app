CREATE OR REPLACE FUNCTION setCode()
RETURNS void AS '
    DECLARE
       row record;
    BEGIN
       IF (SELECT COUNT(*) FROM public.news n WHERE n.news_code IS NULL) > 0 THEN
            DROP SEQUENCE IF EXISTS code_seq;
            CREATE SEQUENCE code_seq
                START 1
                INCREMENT 1;
            FOR row IN SELECT * FROM public.news ORDER BY created_at LOOP
                UPDATE public.news
                SET news_code = NULL
                WHERE id = row.id;
           END LOOP;

           DELETE FROM public.news_code;

           FOR row IN SELECT * FROM public.news ORDER BY created_at LOOP

               INSERT INTO public.news_code (id)
                   VALUES (LPAD(NEXTVAL(''code_seq'')::TEXT, 7, ''0''));

               UPDATE public.news
                   SET news_code = LPAD(CURRVAL(''code_seq'')::TEXT, 7, ''0'')
                       WHERE id = row.id AND news_code IS NULL;

           END LOOP;
       END IF;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION convertToNonSigned(str text, isMatchCase boolean)
RETURNS text AS '
    DECLARE
        originalChars text;
        replacementChars text;
        result text;
    BEGIN
        originalChars = ''áàảãạâấầẩẫậăắằẳẵặđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶĐÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴ'';
        replacementChars = ''aaaaaaaaaaaaaaaaadeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyAAAAAAAAAAAAAAAAADEEEEEEEEEEEIIIIIOOOOOOOOOOOOOOOOOUUUUUUUUUUUYYYYY'';
        result = str;

        IF isMatchCase THEN

        ELSE
            result = lower(result);
            FOR i IN 1..length(originalChars)
                LOOP
                    result = replace(result, substr(originalChars, i, 1), substr(replacementChars, i, 1));
                END LOOP;
        END IF;

        RETURN result;
    END;
' LANGUAGE plpgsql;


SELECT setCode();