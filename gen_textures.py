from PIL import Image, ImageDraw

# --- Иконка предмета: чашка с чаем 16x16 ---
item = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
d = ImageDraw.Draw(item)
# чашка (белая керамика)
d.rectangle([3, 8, 12, 14], fill=(235, 235, 235, 255))
d.rectangle([3, 13, 12, 14], fill=(200, 200, 200, 255))
# ручка чашки
d.rectangle([12, 9, 14, 12], outline=(200, 200, 200, 255), width=1)
# чай внутри (розоватый — намёк на "мышиный" эффект)
d.rectangle([4, 8, 11, 9], fill=(214, 122, 178, 255))
# пар
d.point((5, 6), fill=(255, 255, 255, 180))
d.point((6, 4), fill=(255, 255, 255, 150))
d.point((9, 5), fill=(255, 255, 255, 180))
d.point((10, 3), fill=(255, 255, 255, 150))
item.save("src/main/resources/assets/mousetea/textures/item/mouse_tea.png")

# --- Текстура для процедурной модели ушей/хвоста/усов: 64x32 ---
parts = Image.new("RGBA", (64, 32), (0, 0, 0, 0))
dp = ImageDraw.Draw(parts)
skin = (222, 176, 186, 255)      # розовая мышиная кожа
skin_dark = (196, 140, 152, 255)
fur = (150, 130, 120, 255)       # серо-бурая шёрстка (используется на кромке ушей)
whisker = (245, 245, 245, 255)

# Левое ухо: texOffs(0,0), box 3x3x1 -> UV-регион разворачивается автоматически CubeListBuilder,
# просто закрашиваем область под неё (0..~12 px по X, 0..~7 px по Y с запасом на все грани).
dp.rectangle([0, 0, 12, 8], fill=skin)
dp.rectangle([0, 0, 12, 2], fill=fur)

# Правое ухо: texOffs(8,0) — перекрывающийся регион в этом черновике, для реального мода
# рекомендуется развести UV через Blockbench, чтобы не было наложений.
dp.rectangle([16, 0, 28, 8], fill=skin)
dp.rectangle([16, 0, 28, 2], fill=fur)

# Хвост: texOffs(0,8), box 1x1x10 (длинный, тонкий) — розовая полоса с лёгким затемнением по краям.
dp.rectangle([0, 8, 20, 12], fill=skin_dark)
for x in range(0, 20, 4):
    dp.line([(x, 8), (x, 12)], fill=skin)

# Усы: texOffs(0,20), box 8x1x1 — белые волоски на прозрачном фоне.
dp.rectangle([0, 20, 16, 21], fill=whisker)

parts.save("src/main/resources/assets/mousetea/textures/entity/mouse_parts.png")
print("OK")
