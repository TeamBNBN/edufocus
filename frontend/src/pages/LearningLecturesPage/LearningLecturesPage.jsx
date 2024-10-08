import styles from './LearningLecturesPage.module.css';
import { Link } from 'react-router-dom';
import { useMyLectures } from '../../hooks/api/useMyLectures';
import CompassIcon from '/src/assets/icons/compass.svg?react';
import { STATIC_URL } from '../../constants';
import useBoundStore from '../../store';

export default function LearningLecturesPage() {
  const { data } = useMyLectures();
  const onGoingClasses = data?.data ?? [];
  const hasOnGoingClasses = onGoingClasses.length > 0;
  const userType = useBoundStore((state) => state.userType);
  const myLectureTitle = userType === 'student' ? '수강중인 강의' : '내 강의';

  return (
    <section>
      <h2 className={styles.title}>{myLectureTitle}</h2>
      <div className={styles.grid}>
        {hasOnGoingClasses ? (
          onGoingClasses.map?.((lecture) => (
            <Link
              key={lecture.id}
              to={`/lecture/${lecture.id}/class`}
              className={styles.card}
            >
              <div className={styles.thumbnail}>
                {lecture.image ? (
                  <img
                    src={`${STATIC_URL}${lecture.image}`}
                    alt={lecture.title}
                    className={styles.thumbnail}
                  />
                ) : (
                  <CompassIcon />
                )}
              </div>
              <div className={styles.title}>{lecture.title}</div>
            </Link>
          ))
        ) : (
          <div className={styles.empty}>수강중인 강의가 없습니다.</div>
        )}
      </div>
    </section>
  );
}
