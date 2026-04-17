// src/features/familias/types/familia.ts

export type Sexo = "MASCULINO" | "FEMININO";

export type Parentesco =
  | "RESPONSAVEL"
  | "CONJUGE"
  | "FILHO"
  | "FILHA"
  | "PAI"
  | "MAE"
  | "AVO"
  | "NETO"
  | "IRMAO"
  | "IRMA"
  | "TIO"
  | "TIA"
  | "SOBRINHO"
  | "SOBRINHA"
  | "PRIMO"
  | "PRIMA"
  | "ENTEADO"
  | "ENTEADA"
  | "GENRO"
  | "NORA"
  | "SOGRO"
  | "SOGRA"
  | "CUNHADO"
  | "CUNHADA"
  | "OUTRO";

export type SituacaoFamilia =
  | "ATIVA"
  | "INATIVA"
  | "SUSPENSA"
  | "DESLIGADA";

export type LocalizacaoDomicilio =
  | "URBANA"
  | "RURAL";

export interface EnderecoDTO {
  logradouro: string;
  numero: string;
  bairro: string;
  cidade: string;
  uf: string;
  cep: string;
  pontoReferencia?: string;
  localizacaoDomicilio: LocalizacaoDomicilio;
}

export interface PessoaDTO {
  id?: number;
  nome: string;
  cpf: string;
  telefone?: string;
  sexo: Sexo;
  parentesco: Parentesco;
  rendaMensal?: number;
  dataNascimento: string;
  numeroRg?: string;
  orgaoExpeditorRg?: string;
  dataExpedicaoRg?: string;
  referencia: boolean;
  endereco?: EnderecoDTO;
}

export interface FamiliaDTO {
  id?: number;
  pessoaReferencia: PessoaDTO;
  membrosDaFamilia: PessoaDTO[];
  rendaFamiliar?: number;
  codigoCadunico?: string;
  situacao: SituacaoFamilia;
}

// Labels para exibição
export const SEXO_LABELS: Record<Sexo, string> = {
  MASCULINO: "Masculino",
  FEMININO: "Feminino",
};

export const PARENTESCO_LABELS: Record<Parentesco, string> = {
  RESPONSAVEL: "Responsável",
  CONJUGE: "Cônjuge",
  FILHO: "Filho",
  FILHA: "Filha",
  PAI: "Pai",
  MAE: "Mãe",
  AVO: "Avô/Avó",
  NETO: "Neto",
  IRMAO: "Irmão",
  IRMA: "Irmã",
  TIO: "Tio",
  TIA: "Tia",
  SOBRINHO: "Sobrinho",
  SOBRINHA: "Sobrinha",
  PRIMO: "Primo",
  PRIMA: "Prima",
  ENTEADO: "Enteado",
  ENTEADA: "Enteada",
  GENRO: "Genro",
  NORA: "Nora",
  SOGRO: "Sogro",
  SOGRA: "Sogra",
  CUNHADO: "Cunhado",
  CUNHADA: "Cunhada",
  OUTRO: "Outro",
};

export const SITUACAO_LABELS: Record<SituacaoFamilia, string> = {
  ATIVA: "Ativa",
  INATIVA: "Inativa",
  SUSPENSA: "Suspensa",
  DESLIGADA: "Desligada",
};

export const SITUACAO_COLORS: Record<SituacaoFamilia, string> = {
  ATIVA: "bg-green-100 text-green-800",
  INATIVA: "bg-gray-100 text-gray-800",
  SUSPENSA: "bg-amber-100 text-amber-800",
  DESLIGADA: "bg-red-100 text-red-800",
};

export const LOCALIZACAO_LABELS: Record<LocalizacaoDomicilio, string> = {
  URBANA: "Urbana",
  RURAL: "Rural",
};

export const UF_OPTIONS = [
  "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
  "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
  "RS","RO","RR","SC","SP","SE","TO",
];
