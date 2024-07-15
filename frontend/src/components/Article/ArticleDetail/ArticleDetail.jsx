import { Link } from "react-router-dom";
import styles from './ArticleDetail.module.css'


export default function ArticleDetail() {

    const title = '헷갈리는게 있어요';
    const author = '이재용';
    const content = 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Perferendis sed dolorem vitae?';
    // TODO : -, --를 자리에 맞는 아이콘으로 변경
    return (
        <>
        <div className={styles.articleDetail}>
            <header>
                <div>
                    <Link to={'/'} className={styles.backButton}>
                        <div>-</div>
                        <div className={styles.backText}>Q&A</div>
                    </Link>
                </div>
                <div>
                    <div className={styles.title}>{title}</div>
                    <div className={styles.author}>{author}</div>
                </div>
            </header>
            <div>
                <p className={styles.content}>{content}</p>
            </div>
            <div className={styles.answer}>
                <div className={styles.answerHeader}>
                    <div>--</div>
                    <div className={styles.author}>선생님의 답변</div>
                </div>
                <div className={styles.content}>Lorem ipsum dolor sit amet.</div>
            </div>
        </div>
        </>
    )
}