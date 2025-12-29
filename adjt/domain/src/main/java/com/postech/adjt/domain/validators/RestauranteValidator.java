package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.constants.TamanhoUtil;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;

/**
 * Validador de domínio para a entidade {@link Restaurante}.
 * <p>
 * Esta classe é responsável por validar as restrições de tamanho, obrigatoriedade de campos
 * e regras de permissão de acesso (autorização de domínio).
 */
public class RestauranteValidator {

    /**
     * Valida integralmente um objeto Restaurante e a permissão do usuário logado.
     *
     * @param restaurante       O objeto Restaurante a ser validado.
     * @param idUsuarioLogado    O identificador do usuário que está requisitando a operação.
     * @throws IllegalArgumentException Caso qualquer regra de negócio ou restrição de tamanho seja violada.
     */
    public static void validar(Restaurante restaurante, Integer idUsuarioLogado) {

        // 1. Validação de existência do objeto
        if (restaurante == null) {
            throw new IllegalArgumentException(MensagemUtil.RESTAURANTE_NULO);
        }

        // 2. Validação do Nome (Obrigatoriedade e Comprimento)
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.NOME_RESTAURANTE_OBRIGATORIO);
        }

        if (restaurante.getNome().length() < TamanhoUtil.NOME_MINIMO_LENGTH) {
            throw new IllegalArgumentException(MensagemUtil.NOME_MINIMO_CARACTERES);
        }

        if (restaurante.getNome().length() > TamanhoUtil.NOME_MAXIMO_LENGTH) {
            throw new IllegalArgumentException(MensagemUtil.NOME_MAXIMO_CARACTERES);
        }

        // 3. Validação do Horário (Delegada ao validador especializado)
        if (restaurante.getHorarioFuncionamento() == null || restaurante.getHorarioFuncionamento().trim().isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.HORARIO_FUNCIONAMENTO_OBRIGATORIO);
        }

        // 4. Validação do Tipo de Cozinha
        if (restaurante.getTipoCozinha() == null) {
            throw new IllegalArgumentException(MensagemUtil.TIPO_COZINHA_OBRIGATORIO);
        }

        // 5. Regras de Permissão e Propriedade (Autorização)
        if (restaurante.getDono() == null) {
            throw new IllegalArgumentException(MensagemUtil.DONO_RESTAURANTE_OBRIGATORIO);
        }

        // Verifica se o usuário logado é de fato o proprietário deste restaurante
        if (!restaurante.getDono().getId().equals(idUsuarioLogado)) {
            throw new IllegalArgumentException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }

        // Verifica se o usuário possui o perfil/papel de "Dono de Restaurante"
        if(!(restaurante.getDono().getTipoUsuario() instanceof TipoUsuarioDonoRestaurante)) {
            throw new IllegalArgumentException(MensagemUtil.USUARIO_NAO_E_DONO_RESTAURANTE);
        }

        // 6. Validação de Descrição (Opcional, mas com limite de tamanho)
        if (restaurante.getDescricao() != null && restaurante.getDescricao().length() > TamanhoUtil.DESCRICAO_MAXIMA_LENGTH) {
            throw new IllegalArgumentException(MensagemUtil.DESCRICAO_MAXIMO_CARACTERES);
        }

        // 7. Validação lógica da estrutura do JSON de Horários
        HorarioRestauranteValidator.validar(restaurante.getHorarioFuncionamento());
    }
}