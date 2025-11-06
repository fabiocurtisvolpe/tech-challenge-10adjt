package com.postech.adjt.domain.exception;

/**
 * Exceção personalizada para representar falhas relacionadas ao processo de
 * notificação.
 * 
 * <p>
 * Essa exceção é utilizada para sinalizar erros específicos durante o envio,
 * recebimento ou processamento de notificações dentro da aplicação.
 * </p>
 * 
 * <p>
 * Deve ser capturada e tratada pelo
 * {@link com.postech.adjt.exception.GlobalExceptionHandler}
 * para garantir respostas padronizadas ao cliente.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
public class NotificacaoException extends RuntimeException {

    /**
     * Construtor da exceção com mensagem personalizada.
     *
     * @param message Mensagem explicativa sobre o erro ocorrido.
     */
    public NotificacaoException(String message) {
        super(message);
    }
}
