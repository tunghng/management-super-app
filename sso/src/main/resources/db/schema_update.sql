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
