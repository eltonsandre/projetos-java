package br.com.db1.service;

import java.io.Serializable;

import br.com.db1.model.Complexidade;

public class SenhaService implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String ALPHAS = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERICS = "01234567890";
	private static final String SYMBOLS = ")!@#$%^&*()";

	private Integer percentScore = 0;
	private Complexidade complexidade = Complexidade.MUITO_CURTA;

	public void validate(String senha) {

		Integer pontuacao = 0, tamanho = 0, alphaMaiusculo = 0, alphaMinusculo = 0, numero = 0, simbolo = 0;

		Integer nMidChar = 0, nRequisitos = 0, nUnqChar = 0, charConsecutivo = 0, charRepetido = 0;
		Integer alphaMaiusculoConsecutivo = 0, alphaMinusculoConsecutivo = 0, numeroConsecutivo = 0;
		Integer sequenciaDeAlpha = 0, sequenciaDeNumero = 0, sequenciaSimbolos = 0, sequenciaDeChar = 0;

		Integer nMultMidChar = 2, multAlphaMaiusculoConsecutivo = 2, multAlphaMinusculoConsecutivo = 2,
				multNumeroConsecutivo = 2;

		Integer nMultSeqAlpha = 3, nMultSeqNumber = 3, nMultSeqSymbol = 3;
		Integer nMultLength = 4, nMultNumber = 4;
		Integer nMultSymbol = 6;

		Integer nTmpAlphaUC = null, nTmpAlphaLC = null, nTmpNumber = null, nTmpSymbol = null;

		Integer nMinPwdLen = 8;

		if (senha == null) {
			this.complexidade = Complexidade.MUITO_CURTA;
			this.percentScore = 0;

		} else {

			tamanho = senha.length();
			pontuacao = tamanho * nMultLength;

			String[] arrayDeSenha = senha.replaceAll("\\s+", "").split("\\s*");
			int tamanhoDaSenha = arrayDeSenha.length;

			/*
			 * Loop atrav�s de senha para verificar se h� coincid�ncias de
			 * s�mbolos, n�meros, min�sculas e mai�sculas
			 */
			for (int index1 = 0; index1 < tamanhoDaSenha; index1++) {

				if (arrayDeSenha[index1].matches("([A-Z])")) {
					if (nTmpAlphaUC != null) {
						if ((nTmpAlphaUC + 1) == index1) {
							alphaMaiusculoConsecutivo++;
						}
					}
					nTmpAlphaUC = index1;
					alphaMaiusculo++;

				} else if (arrayDeSenha[index1].matches("([a-z])")) {
					if (nTmpAlphaLC != null) {
						if ((nTmpAlphaLC + 1) == index1) {
							alphaMinusculoConsecutivo++;
						}
					}
					nTmpAlphaLC = index1;
					alphaMinusculo++;

				} else if (arrayDeSenha[index1].matches("([0-9])")) {
					if (index1 > 0 && index1 < (tamanhoDaSenha - 1)) {
						nMidChar++;
					}

					if (nTmpNumber != null) {
						if ((nTmpNumber + 1) == index1) {
							numeroConsecutivo++;
						}
					}
					nTmpNumber = index1;
					numero++;

				} else if (arrayDeSenha[index1].matches("([^a-zA-Z0-9_]*)")) {
					if (index1 > 0 && index1 < (tamanhoDaSenha - 1)) {
						nMidChar++;
					}

					if (nTmpSymbol != null) {
						if ((nTmpSymbol + 1) == index1) {
						}
					}
					nTmpSymbol = index1;
					simbolo++;
				}

				/*
				 * Loop interno atrav�s de senha para verificar caracteres de
				 * repeti��o
				 */
				Boolean isCharExiste = Boolean.FALSE;
				for (int index2 = 0; index2 < tamanhoDaSenha; index2++) {

					if (arrayDeSenha[index1] == arrayDeSenha[index2] && index1 != index2) {

						// Existe um caractere de repeti��o
						isCharExiste = true;
						/*
						 * Calcula a dedu��o da dedu��o com base na proximidade
						 * de caracteres id�nticos A dedu��o � incrementada cada
						 * vez que uma nova correspond�ncia � descoberta O
						 * montante da dedu��o � calculado com base no
						 * comprimento total da palavra-passe dividido pela
						 * diferen�a de dist�ncia entre a correspond�ncia
						 * seleccionada
						 *
						 */
						charRepetido += Math.abs(tamanhoDaSenha / (index2 - index1));
					}
				}

				if (isCharExiste) {
					charConsecutivo++;
					nUnqChar = tamanhoDaSenha - charConsecutivo;
					charRepetido = (int) ((nUnqChar != 0) ? Math.ceil(charRepetido / nUnqChar)
							: Math.ceil(charRepetido));
				}
			}

			/*
			 * Verifique se h� padr�es seq�enciais de seq��ncia alfa (frente e
			 * verso)
			 */
			for (int s = 0; s < 23; s++) {
				String frente = ALPHAS.substring(s, s + 3);
				String verso = new StringBuilder(frente).reverse().toString();

				if (senha.toLowerCase().indexOf(frente) != -1 || senha.toLowerCase().indexOf(verso) != -1) {
					sequenciaDeAlpha++;
				}
			}

			/*
			 * Verifique se h� padr�es de seq��ncia num�rica sequencial (frente
			 * e verso)
			 */
			for (int s = 0; s < 8; s++) {
				String frente = NUMERICS.substring(s, s + 3);
				String verso = new StringBuilder(frente).reverse().toString();

				if (senha.toLowerCase().indexOf(frente) != -1 || senha.toLowerCase().indexOf(verso) != -1) {
					sequenciaDeNumero++;
				}
			}

			/*
			 * Verifique se h� padr�es seq�enciais de seq��ncia de s�mbolos
			 * (para frente e para tr�s)
			 */
			for (int s = 0; s < 8; s++) {
				String frente = SYMBOLS.substring(s, s + 3);
				String verso = new StringBuilder(frente).reverse().toString();

				if (senha.toLowerCase().indexOf(frente) != -1 || senha.toLowerCase().indexOf(verso) != -1) {
					sequenciaSimbolos++;
				}
			}

			// Modificar o valor da pontua��o global com base no uso versus
			// requisitos
			if (alphaMaiusculo > 0 && alphaMaiusculo < tamanho) {
				pontuacao = pontuacao + ((tamanho - alphaMaiusculo) * 2);
			}
			if (alphaMinusculo > 0 && alphaMinusculo < tamanho) {
				pontuacao = (pontuacao + ((tamanho - alphaMinusculo) * 2));
			}
			if (numero > 0 && numero < tamanho) {
				pontuacao = (pontuacao + (numero * nMultNumber));
			}
			if (simbolo > 0) {
				pontuacao = (pontuacao + (simbolo * nMultSymbol));
			}
			if (nMidChar > 0) {
				pontuacao = (pontuacao + (nMidChar * nMultMidChar));
			}

			// Dedu��es de pontos por pr�ticas ruins
			if ((alphaMinusculo > 0 || alphaMaiusculo > 0) && simbolo == 0 && numero == 0) {
				pontuacao = (pontuacao - tamanho);
			}

			if (alphaMinusculo == 0 && alphaMaiusculo == 0 && simbolo == 0 && numero > 0) {
				pontuacao = (pontuacao - tamanho);
			}

			// O mesmo caractere existe mais de uma vez
			if (charConsecutivo > 0) {
				pontuacao = (pontuacao - charRepetido);
			}

			// Existem letras mai�sculas consecutivas
			if (alphaMaiusculoConsecutivo > 0) {
				pontuacao = (pontuacao - (alphaMaiusculoConsecutivo * multAlphaMaiusculoConsecutivo));
			}

			// existem letras min�sculas consecutive
			if (alphaMinusculoConsecutivo > 0) {
				pontuacao = (pontuacao - (alphaMinusculoConsecutivo * multAlphaMinusculoConsecutivo));
			}

			// existem n�meros Consecutivos
			if (numeroConsecutivo > 0) {
				pontuacao = (pontuacao - (numeroConsecutivo * multNumeroConsecutivo));
			}

			// Seq��ncias de caracteres alfa existem (3 caracteres ou mais)
			if (sequenciaDeAlpha > 0) {
				pontuacao = (pontuacao - (sequenciaDeAlpha * nMultSeqAlpha));
			}

			// Seq��ncias de caracteres num�ricas existem (3 caracteres ou mais)
			if (sequenciaDeNumero > 0) {
				pontuacao = (pontuacao - (sequenciaDeNumero * nMultSeqNumber));
			}

			// Existem seq��ncias de caracteres de s�mbolos (3 caracteres ou
			// mais)
			if (sequenciaSimbolos > 0) {
				pontuacao = (pontuacao - (sequenciaSimbolos * nMultSeqSymbol));
			}

			/*
			 * Determinar se os requisitos obrigat�rios foram atendidos e
			 * definir indicadores de imagem de acordo
			 */
			Integer[] arrayChars = { tamanho, alphaMaiusculo, alphaMinusculo, numero, simbolo };
			String[] arrayIDsChars = { "nLength", "nAlphaUC", "nAlphaLC", "nNumber", "nSymbol" };
			Integer tamanhoArrayDeChars = arrayChars.length;

			for (Integer c = 0; c < tamanhoArrayDeChars; c++) {
				int minVal;
				if ("nLength".equals(arrayIDsChars[c])) {
					minVal = (nMinPwdLen - 1);
				} else {
					minVal = 0;
				}

				if (arrayChars[c].equals(minVal + 1)) {
					sequenciaDeChar++;
				} else if (arrayChars[c] > (minVal + 1)) {
					sequenciaDeChar++;
				}
			}

			nRequisitos = sequenciaDeChar;

			Integer nMinReqChars;
			if (senha.length() >= nMinPwdLen) {
				nMinReqChars = 3;
			} else {
				nMinReqChars = 4;
			}

			// existem um ou mais caracteres necess�rios
			if (nRequisitos > nMinReqChars) {
				pontuacao = (pontuacao + (nRequisitos * 2));
			}

			this.complexidade = determinarComplexidade(pontuacao);
			this.percentScore = pontuacao > 100 ? 100 : pontuacao;
		}
	}

	/**
	 * Determine a complexidade com base na pontua��o geral
	 *
	 * @param pontuacao
	 */
	private Complexidade determinarComplexidade(Integer pontuacao) {

		Complexidade complexidade = Complexidade.MUITO_CURTA;
		if (pontuacao > 100) {
			pontuacao = 100;
		} else if (pontuacao < 0) {
			pontuacao = 0;
		}
		if (pontuacao >= 0 && pontuacao < 20) {
			complexidade = Complexidade.MUITO_FRACO;
		} else if (pontuacao >= 20 && pontuacao < 40) {
			complexidade = Complexidade.FRACO;
		} else if (pontuacao >= 40 && pontuacao < 60) {
			complexidade = Complexidade.BOA;
		} else if (pontuacao >= 60 && pontuacao < 80) {
			complexidade = Complexidade.FORTE;
		} else if (pontuacao >= 80 && pontuacao <= 100) {
			complexidade = Complexidade.MUITO_FORTE;
		}

		return complexidade;
	}

	public Integer getPercentScore() {
		return percentScore;
	}

	public Complexidade getComplexidade() {
		return complexidade;
	}

	public static void main(String[] args) {
		SenhaService checkPass = new SenhaService();
		checkPass.validate("S@nDr�2808");

		System.out.println(checkPass.getComplexidade().getDescricao() + " " + checkPass.getPercentScore() + "%");
	}
}
