import { Link } from "react-router-dom"
import styles from "./ArticlePreview.module.css"

export default function ArticlePreview () {
   // TODO: 아이콘 변경
    return (
        <div className={styles.wrapper}>
            <div className={styles.header}>
                <div className={styles.title}>공지사항</div>
                <Link to="/">ICON</Link>
            </div>
            <div className={styles.main}>
                <div className={styles.content}>3주차 수업 휴강 공지</div>
                <div className={styles.content}>정보처리기사 어쩌구</div>
                <div className={styles.content}>첫 수업 준비사항</div>
            </div>
        </div>
    )
}