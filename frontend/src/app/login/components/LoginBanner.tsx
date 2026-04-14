import Image from "next/image";

export function LoginBanner() {
  return (
    <div className="relative hidden lg:flex lg:w-[55%] xl:w-[60%] flex-col items-center justify-center overflow-hidden">
      {/* Imagem de fundo */}
      <Image
        src="/images/banner-login.jpg"
        alt="Vista de Ribeirão"
        fill
        className="object-cover opacity-30"
        priority
      />

      {/* Gradiente */}
      <div className="absolute inset-0 bg-gradient-to-b from-brand-dark-blue/10 via-brand-dark-blue/40 to-brand-dark-blue/80" />

      {/* Conteúdo */}
      <div className="relative z-10 flex flex-col items-center px-12">
        {/* Logo */}
        <div className="mb-8 flex flex-col items-center gap-5 rounded-2xl bg-white/85 px-10 py-8 shadow-xl backdrop-blur-sm animate-fade-in">
          <Image
            src="/images/logo-cad-circular.png"
            alt="Logo CAD Ribeirão"
            width={160}
            height={160}
            priority
          />
        </div>

        {/* Título */}
        <h1
          className="mb-4 text-center text-3xl font-bold text-white drop-shadow-lg animate-fade-in xl:text-4xl"
          style={{ animationDelay: "0.2s" }}
        >
          Sistema de Cadastro
          <br />
          <span className="text-brand-yellow">Assistência Social</span>
        </h1>

        <p
          className="max-w-md text-center text-base text-white/80 drop-shadow-md animate-fade-in"
          style={{ animationDelay: "0.3s" }}
        >
          Gestão integrada de famílias, programas sociais e acompanhamento
          de vulnerabilidade do município de Ribeirão.
        </p>

        {/* Indicadores */}
        <div
          className="mt-8 grid grid-cols-3 gap-4 animate-fade-in"
          style={{ animationDelay: "0.4s" }}
        >
          {[
            { label: "Famílias", value: "2.450+" },
            { label: "Atendimentos", value: "8.320+" },
            { label: "Programas", value: "12" },
          ].map((stat) => (
            <div
              key={stat.label}
              className="rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-center backdrop-blur-sm"
            >
              <p className="text-xl font-bold text-brand-yellow drop-shadow">
                {stat.value}
              </p>
              <p className="text-xs text-white/70">{stat.label}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
