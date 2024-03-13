CREATE OR REPLACE FUNCTION setFormCode()
RETURNS void AS '
    DECLARE
       row record;
    BEGIN
	   IF (SELECT MIN(length(id)) FROM public.form_code) != 7 OR (SELECT MAX(length(id)) FROM public.form_code) != 7 THEN
	       DROP SEQUENCE IF EXISTS form_code_seq;
		   CREATE SEQUENCE form_code_seq
               START 1
               INCREMENT 1;

		   FOR row IN SELECT * FROM public.form LOOP
	   	       UPDATE public.form
		       SET code = NULL
		       WHERE id = row.id;
           END LOOP;

	       DELETE FROM public.form_code;

	       FOR row IN SELECT * FROM public.form LOOP
	   	       INSERT INTO public.form_code (id)
               VALUES (LPAD(NEXTVAL(''form_code_seq'')::TEXT, 7, ''0''));
               UPDATE public.form
               SET code = LPAD(CURRVAL(''form_code_seq'')::TEXT, 7, ''0'')
               WHERE id = row.id AND code IS NULL;
           END LOOP;
	   END IF;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION setFormTemplateCode()
RETURNS void AS '
    DECLARE
       row record;
    BEGIN
	   IF (SELECT MIN(length(id)) FROM public.form_template_code) != 7 OR (SELECT MAX(length(id)) FROM public.form_template_code) != 7 THEN
	       DROP SEQUENCE IF EXISTS form_template_code_seq;
		   CREATE SEQUENCE form_template_code_seq
               START 1
               INCREMENT 1;

		   FOR row IN SELECT * FROM public.form_template LOOP
	   	       UPDATE public.form_template
		       SET code = NULL
		       WHERE id = row.id;
           END LOOP;

	       DELETE FROM public.form_template_code;

	       FOR row IN SELECT * FROM public.form_template LOOP
	   	       INSERT INTO public.form_template_code (id)
               VALUES (LPAD(NEXTVAL(''form_template_code_seq'')::TEXT, 7, ''0''));
               UPDATE public.form_template
               SET code = LPAD(CURRVAL(''form_template_code_seq'')::TEXT, 7, ''0'')
               WHERE id = row.id AND code IS NULL;
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


SELECT setFormCode();
SELECT setFormTemplateCode();