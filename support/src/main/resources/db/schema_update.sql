CREATE OR REPLACE FUNCTION setCode()
RETURNS void AS '
    DECLARE
       row record;
    BEGIN
       IF (SELECT COUNT(*) FROM public.ticket) > (SELECT COUNT(*) FROM public.ticket_code) THEN
           FOR row IN SELECT * FROM public.ticket ORDER BY created_at LOOP
               INSERT INTO public.ticket_code (id)
                   VALUES (LPAD(NEXTVAL(''code_seq'')::TEXT, 7, ''0''));
               UPDATE public.ticket
                   SET ticket_code = LPAD(CURRVAL(''code_seq'')::TEXT, 7, ''0'')
                       WHERE id = row.id AND ticket_code IS NULL;
           END LOOP;
       END IF;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION setIsDeleted()
RETURNS void AS '
    DECLARE
       row record;
    BEGIN
        FOR row IN SELECT * FROM public.ticket ORDER BY created_at LOOP
            UPDATE public.ticket
            SET is_deleted = FALSE
               WHERE id = row.id AND is_deleted IS NULL;
        END LOOP;
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
SELECT setIsDeleted();