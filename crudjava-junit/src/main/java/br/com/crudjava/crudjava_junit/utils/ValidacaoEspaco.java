package br.com.crudjava.crudjava_junit.utils;

public class ValidacaoEspaco {

        public static boolean validarArea(String areaStr) {
            if (areaStr == null || areaStr.trim().isEmpty()) {
                return false;
            }
            String areaNormalizada = areaStr.trim().replace(",", ".");

            try {
                double area = Double.parseDouble(areaNormalizada);
                return area > 0;

            } catch (NumberFormatException e) {
                return false;
            }
        }
        public static boolean validarPiso(String pisoStr) {
            if (pisoStr == null || pisoStr.trim().isEmpty()) {
                return false;
            }

            String pisoNormalizado = pisoStr.trim();
            return pisoNormalizado.equals("1") || pisoNormalizado.equals("2");
        }
        public static boolean validarSelecaoEdicao(int quantidadeSelecionada) {
            return quantidadeSelecionada == 1;
        }

        public static boolean validarSelecaoRemocao(int quantidadeSelecionada) {
            return quantidadeSelecionada > 0;
        }
}
