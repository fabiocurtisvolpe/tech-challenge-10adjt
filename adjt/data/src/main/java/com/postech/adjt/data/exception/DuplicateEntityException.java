package com.postech.adjt.data.exception;

/**
 * Exceção personalizada para indicar que uma entidade já existe no sistema.
 *
 * <p>
 * Utilizada em cenários onde uma operação de criação ou atualização
 * viola regras de unicidade, como duplicidade de login, e-mail ou nome.
 * </p>
 *
 * <p>
 * Essa exceção pode ser capturada por um {@code @ControllerAdvice}
 * para retornar uma resposta HTTP apropriada ao cliente.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
public class DuplicateEntityException extends RuntimeException {

    /**
     * Construtor da exceção com mensagem personalizada.
     *
     * @param message Mensagem explicativa sobre o conflito de duplicidade.
     */
    public DuplicateEntityException(String message) {
        super(message);
    }
}