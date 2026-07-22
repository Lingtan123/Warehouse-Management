from pathlib import Path

import pypdfium2 as pdfium
from PIL import Image
from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.utils import ImageReader
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import Paragraph
from reportlab.pdfgen import canvas


ROOT = Path(__file__).resolve().parents[2]
DOCS_DIR = ROOT / "docs"
TMP_DIR = ROOT / "tmp" / "pdfs" / "tailored_resume"
TMP_DIR.mkdir(parents=True, exist_ok=True)

SOURCE_PDF = next(DOCS_DIR.glob("*.pdf"))
OUTPUT_PDF = DOCS_DIR / "我的简历_AI应用开发版.pdf"
PHOTO_PATH = TMP_DIR / "photo_crop.png"


def register_fonts() -> None:
    pdfmetrics.registerFont(TTFont("MSYH", r"C:\Windows\Fonts\msyh.ttc"))
    pdfmetrics.registerFont(TTFont("MSYH-Bold", r"C:\Windows\Fonts\msyhbd.ttc"))


def ensure_photo() -> Path:
    if PHOTO_PATH.exists():
        return PHOTO_PATH

    pdf = pdfium.PdfDocument(str(SOURCE_PDF))
    bitmap = pdf[0].render(scale=4)
    image = bitmap.to_pil()
    width, height = image.size

    crop = image.crop(
        (
            int(width * 0.835),
            int(height * 0.03),
            int(width * 0.96),
            int(height * 0.175),
        )
    )
    crop.save(PHOTO_PATH)
    return PHOTO_PATH


def build_styles() -> dict[str, ParagraphStyle]:
    return {
        "body": ParagraphStyle(
            "body",
            fontName="MSYH",
            fontSize=10.2,
            leading=15.4,
            textColor=colors.HexColor("#222222"),
            alignment=TA_LEFT,
        ),
        "body_bullet": ParagraphStyle(
            "body_bullet",
            parent=ParagraphStyle("tmp", fontName="MSYH", fontSize=10.2, leading=15.4),
            leftIndent=14,
            firstLineIndent=-14,
            spaceAfter=2,
            textColor=colors.HexColor("#222222"),
        ),
        "small": ParagraphStyle(
            "small",
            fontName="MSYH",
            fontSize=9.8,
            leading=14.8,
            textColor=colors.HexColor("#333333"),
            alignment=TA_LEFT,
        ),
        "title": ParagraphStyle(
            "title",
            fontName="MSYH-Bold",
            fontSize=27,
            leading=30,
            alignment=TA_CENTER,
            textColor=colors.black,
        ),
        "section": ParagraphStyle(
            "section",
            fontName="MSYH-Bold",
            fontSize=15.5,
            leading=18,
            textColor=colors.black,
        ),
    }


def draw_paragraph(pdf: canvas.Canvas, text: str, style: ParagraphStyle, x: float, y: float, width: float) -> float:
    para = Paragraph(text, style)
    para_width, para_height = para.wrap(width, 1000)
    para.drawOn(pdf, x, y - para_height)
    return y - para_height


def draw_section_header(pdf: canvas.Canvas, title: str, x: float, y: float) -> float:
    pdf.setFillColor(colors.HexColor("#ef4b4b"))
    pdf.rect(x, y - 23, 3.8, 22, stroke=0, fill=1)
    pdf.setFillColor(colors.black)
    pdf.setFont("MSYH-Bold", 15.5)
    pdf.drawString(x + 8, y - 16, title)
    return y - 30


def main() -> None:
    register_fonts()
    photo_path = ensure_photo()
    styles = build_styles()

    pdf = canvas.Canvas(str(OUTPUT_PDF), pagesize=A4)
    width, height = A4

    left = 42
    content_width = width - 84
    pdf.setFont("MSYH-Bold", 27)
    pdf.setFillColor(colors.black)
    pdf.drawCentredString(width / 2, height - 54, "个人简历")

    y = height - 92

    pdf.setFont("MSYH", 12.5)
    pdf.setFillColor(colors.HexColor("#222222"))
    pdf.drawCentredString(width / 2 - 20, y, "18291272236   |   2338636515@qq.com")
    y -= 22
    pdf.drawCentredString(width / 2 - 20, y, "20岁   |   男   |   应届生   |   AI应用开发实习生   |   共青团员")

    photo = Image.open(photo_path)
    pdf.drawImage(ImageReader(photo), width - 136, height - 157, width=72, height=116, mask="auto")

    y -= 30

    y = draw_section_header(pdf, "教育背景", left, y)
    pdf.setFont("MSYH-Bold", 14)
    pdf.drawString(left, y - 8, "西安电子科技大学")
    pdf.setFont("MSYH", 12.3)
    pdf.drawCentredString(width / 2, y - 8, "计算机科学与技术 (本科)")
    pdf.drawRightString(width - left, y - 8, "2023.09 - 2027.06")
    pdf.setFont("MSYH-Bold", 12.4)
    pdf.drawString(left + 4, y - 30, "•  GPA 3.4/4.0")
    y -= 44

    y = draw_section_header(pdf, "专业技能", left, y)
    skill_bullets = [
        "•  <b>扎实掌握 Java 基础、集合框架、并发工具与 JVM 基础原理</b>，具备良好的工程编码规范意识和问题排查能力。",
        "•  <b>熟练使用 Spring Boot、MyBatis-Plus 开发业务接口</b>，能够完成 RESTful API 设计、参数校验、事务处理与前后端联调。",
        "•  <b>熟悉 MySQL、Redis 的表结构设计、索引与缓存使用</b>，能够围绕结构化数据完成分页检索、查询优化与数据一致性处理。",
        "•  <b>具备前后端分离项目经验</b>，了解 Vue2、Element UI 页面与接口调用流程，能够处理 JSON 等常见数据格式并配合 GitHub 进行版本协作。",
        "•  <b>持续关注 AI 应用开发方向</b>，了解 Prompt、RAG、Agent 等基础概念，愿意从 API 集成、数据处理和知识库检索场景切入实践。",
    ]
    for bullet in skill_bullets:
        y = draw_paragraph(pdf, bullet, styles["body_bullet"], left + 2, y, content_width - 4) - 2

    y -= 4
    y = draw_section_header(pdf, "项目经历", left, y)
    pdf.setFont("MSYH-Bold", 14)
    pdf.drawString(left, y - 8, "集成仓库管理系统")
    pdf.drawCentredString(width / 2, y - 8, "后端开发")
    pdf.setFont("MSYH", 12.3)
    pdf.drawRightString(width - left, y - 8, "2026.01 - 2026.06")
    y -= 22

    y = draw_paragraph(
        pdf,
        "<b>项目简介：</b>基于 Spring Boot + MyBatis-Plus + MySQL + Vue2 的仓库管理系统，采用前后端分离架构，围绕用户、仓库、分类、商品及出入库记录等模块，实现了登录认证、角色菜单控制、分页查询、库存联动与基础数据管理，并完成前后端联调与功能闭环。",
        styles["small"],
        left,
        y,
        content_width,
    ) - 4
    y = draw_paragraph(
        pdf,
        "<b>技术栈：</b>Java、Spring Boot、MyBatis-Plus、MySQL、Redis、Maven、Vue2、Element UI",
        styles["small"],
        left,
        y,
        content_width,
    ) - 4
    y = draw_paragraph(
        pdf,
        "<b>GitHub 地址：</b>https://github.com/Lingtan123/Warehouse-Management.git",
        styles["small"],
        left,
        y,
        content_width,
    ) - 1
    y = draw_paragraph(pdf, "<b>技术亮点：</b>", styles["small"], left, y, content_width) - 2

    project_bullets = [
        "•  基于 <b>Spring Boot + MyBatis-Plus</b> 搭建后端服务，使用 BaseMapper、ServiceImpl、LambdaQueryWrapper 和分页插件完成用户、仓库、分类、商品及出入库记录等模块的 CRUD 与条件查询开发。",
        "•  使用 <b>MySQL</b> 完成用户、商品、仓库、分类、记录等核心表结构设计，并结合业务需求实现基础查询与关联查询，形成清晰的结构化数据组织方式。",
        "•  封装 <b>分页检索与多表联查</b>，输出统一接口数据结构，提升后台列表查询体验，也为后续数据检索与自动化处理提供了良好的接口基础。",
        "•  实现 <b>角色菜单控制、动态路由与库存联动逻辑</b>，在权限隔离的同时完成商品新增、入库、出库时的库存同步与操作流水保留，保证业务记录可追踪、数据状态一致。",
    ]
    for bullet in project_bullets:
        y = draw_paragraph(pdf, bullet, styles["body_bullet"], left + 2, y, content_width - 4) - 1

    y -= 2
    y = draw_section_header(pdf, "自我评价", left, y)
    self_bullets = [
        "•  学习能力和执行力较强，面对新技术能够快速阅读文档、拆解问题并持续推进，能够适应持续投入的实习节奏。",
        "•  具备扎实的后端工程基础与结构化思维，能够从接口、数据库、缓存和业务流程角度理解系统，为转向 AI 应用开发提供工程支撑。",
        "•  对 <b>大模型应用、Vibe Coding、RAG、Agent 与 AI 辅助开发</b> 保持长期兴趣，愿意从 Prompt 调试、API 对接、文档结构化和知识库检索等方向持续学习，并以踏实协作的方式完成任务交付。",
    ]
    for bullet in self_bullets:
        y = draw_paragraph(pdf, bullet, styles["body_bullet"], left + 2, y, content_width - 4) - 1

    pdf.save()
    print(OUTPUT_PDF)


if __name__ == "__main__":
    main()
